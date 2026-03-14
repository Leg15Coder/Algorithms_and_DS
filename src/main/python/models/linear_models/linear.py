from typing import Callable, Iterable

import numpy as np
import pandas as pd

from src.main.python.models.linear_models.loss_functions import mse_loss, LOSS_T
from src.main.python.models.linear_models.base_model import Model
from src.main.python.models.linear_models.regularizers import Regularizer, L2Regularizer, L1Regularizer, EmptyRegularizer


class LinearModel(Model):
    MAX_CACHE = 1 << 16

    def __init__(
            self,
            data_train: pd.DataFrame,
            labels_train: pd.Series,
            loss_func: LOSS_T = mse_loss,
            der_loss_func: LOSS_T = None,
            train: bool = False,
            stop_rule: Callable[[int, float], float] = lambda k, s: k > 1e6 or s < 1e-6,
            initial_step : float = 1e-3,
            step_update_rule: Callable[[pd.DataFrame, np.array, float, int], float] = lambda x, w, s, k: s / 2 if k % 10000 == 0 else s,
            max_gradient: float = 1.,
            regularizer: Regularizer = EmptyRegularizer(),
            initial_momentum_step : float = 1e-2,
            momentum_step_update_rule : Callable[[int, float, float], float] = lambda k, s, v: v * 0.25 + s * 0.999,
            random_seed: int = 42,
            batch_size: int = 1):

        self.data_train = data_train.copy(deep=True)
        self.labels_train = labels_train.copy(deep=True)

        self.mean = data_train.mean()
        self.std = data_train.std().replace(0, 1)
        normalized = (data_train - self.mean) / self.std
        self.data_train = normalized.values
        self.labels_train = labels_train.values

        self.size = (self.data_train.shape[0], self.data_train.shape[1])

        self._weights = np.zeros(self.size[1])
        self._bias = 0
        self.loss_func = loss_func
        self._der_loss_func = der_loss_func
        self.max_gradient = max_gradient

        if self._der_loss_func is None and self.loss_func is not None:
            if (hasattr(self.loss_func, "der") and
                    self.loss_func.der is not None
                    and isinstance(self.loss_func.der, LOSS_T)):
                self._der_loss_func = self.loss_func.der

        self._der_loss_func = np.vectorize(self._der_loss_func)
        if self._der_loss_func is None:
            raise ValueError("Ошибка обучения: не задана производная функции потерь")

        self.generator = np.random.default_rng(random_seed)

        self.stop_rule = stop_rule
        self.initial_step = initial_step
        self.step_update_rule = step_update_rule

        self.regularizer = regularizer
        self.initial_momentum_step = initial_momentum_step
        self.momentum_step_update_rule = momentum_step_update_rule

        self.batch_size = max(1, int(batch_size))

        if train:
            self.train()

    def _f(self, data: np.ndarray | pd.DataFrame) -> np.ndarray:
        assert len(data) == len(self._weights), "Количество признаков в данных не совпадает с количеством весов модели"
        return data.dot(self._weights) + self._bias

    def train(self) -> None:
        self._weights = self.generator.normal(0, 0.1, self.size[1])
        self._bias = 0.0
        step = self.initial_step
        momentum_step = self.initial_momentum_step
        i = 0

        n_samples = self.size[0]

        while not self.stop_rule(i, step):
            if self.batch_size == 1:
                index = self.generator.integers(0, n_samples)
                batch = self.data_train[index:index + 1]
                label_batch = np.array([self.labels_train[index]])
            else:
                index = self.generator.integers(0, n_samples, size=self.batch_size)
                batch = self.data_train[index]
                label_batch = self.labels_train[index]

            der_values = self._der_loss_func(self._f(batch), label_batch)
            der_values = np.asarray(der_values)
            der_values = np.clip(der_values, -self.max_gradient, self.max_gradient)

            mean_der = float(np.mean(der_values))
            momentum_step = self.momentum_step_update_rule(i, momentum_step, mean_der)

            regulator, _b_reg = self.regularizer.der(self._weights, self._bias)
            regulator, _b_reg = momentum_step * regulator, momentum_step * _b_reg

            grads = (der_values.reshape(-1, 1) * batch)
            grad = grads.mean(axis=0) - regulator

            bias_grads = der_values - _b_reg
            bias_grad = bias_grads.mean()

            self._weights -= step * grad
            self._bias -= step * bias_grad

            step = self.step_update_rule(self.data_train, self._weights, step, i)
            i += 1

    def predict(self, data: pd.DataFrame) -> np.ndarray:
        normalized_data = (data - self.mean) / self.std
        data = normalized_data.values
        predictions = data.dot(self._weights) + self._bias
        return np.asarray(predictions, dtype=np.float64)

    def accuracy(self, data: pd.DataFrame, labels: pd.Series) -> float:
        return float(np.mean(1 - (self.predict(data) - labels)))


class LinearClassifier(LinearModel):
    def __init__(self, classes_number: int, exact_classes: Iterable[float] = None, **kwargs):
        super().__init__(**kwargs)
        self.classes_number = classes_number
        self.classes = set(exact_classes) if exact_classes is not None else None
        if self.classes is None or len(self.classes) != classes_number:
            self.classes = set(range(classes_number))

    def _find_closest_class(self, value: float) -> float:
        return min(self.classes, key=lambda c: abs(c - value))

    def predict(self, data: pd.DataFrame) -> np.ndarray:
        # vectorized mapping to closest class without np.vectorize
        continuous_pred = super().predict(data)
        classes_arr = np.array(sorted(self.classes))
        # distances shape (n_samples, n_classes)
        diffs = np.abs(continuous_pred.reshape(-1, 1) - classes_arr.reshape(1, -1))
        idx = np.argmin(diffs, axis=1)
        return classes_arr[idx].astype(np.float64)

    def accuracy(self, data: pd.DataFrame, labels: pd.Series) -> float:
        return float(np.mean(self.predict(data) == labels))


class LinearBinaryClassifier(LinearClassifier):
    def __init__(self, exact_classes: Iterable[float] = None, **kwargs):
        super().__init__(classes_number=2, exact_classes=exact_classes, **kwargs)

    def predict(self, data: pd.DataFrame) -> np.ndarray:
        result = super().predict(data)
        return result


class RidgeRegression(LinearModel):
    def __init__(self, alpha: float = 1e-3, **kwargs):
        super().__init__(loss_func=mse_loss, regularizer=L2Regularizer(alpha), **kwargs)


class LASSORegression(LinearModel):
    def __init__(self, alpha: float = 1e-3, **kwargs):
        super().__init__(loss_func=mse_loss, regularizer=L1Regularizer(alpha), **kwargs)
