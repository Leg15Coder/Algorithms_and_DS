from functools import lru_cache

import numpy as np
import pandas as pd
from abc import ABC
from typing import Callable, Optional

from src.main.python.models.linear_models.base_model import Model


class Kernel(ABC, Callable[[np.ndarray, np.ndarray], float]):
    pass

class LinearKernel(Kernel):
    def __call__(self, x1: np.ndarray, x2: np.ndarray) -> float:
        return np.dot(x1, x2)


class PolynomialKernel(Kernel):
    def __init__(self, degree: int = 3, gamma: float = 1.0, coef0: float = 1.0):
        self.degree = degree
        self.gamma = gamma
        self.coef0 = coef0

    def __call__(self, x1: np.ndarray, x2: np.ndarray) -> float:
        return (self.gamma * np.dot(x1, x2) + self.coef0) ** self.degree


class RBFKernel(Kernel):
    def __init__(self, gamma: float = 1.0):
        self.gamma = gamma

    def __call__(self, x1: np.ndarray, x2: np.ndarray) -> float:
        diff = x1 - x2
        return np.exp(-self.gamma * np.dot(diff, diff))


class SVMModel(Model):
    MAX_CACHE = 1 << 16

    def __init__(
            self,
            data_train: pd.DataFrame,
            labels_train: pd.Series,
            kernel: Kernel = RBFKernel(),
            const: float = 1.0,
            learning_rate: float = 0.01,
            epochs: int = 1000,
            batch_size: Optional[int] = None,
            min_tol: float = 1e-4,
            random_state: int = 42,
            train : bool = False,
            sv_threshold: float = 1e-5,
            delta_epoch: int = 100):

        self.kernel = kernel if kernel else lambda x1, x2: 0
        self.soft_const = const
        self.learning_rate = learning_rate
        self.epochs = epochs
        self.batch_size = batch_size
        self.min_tol = min_tol
        self.sv_threshold = sv_threshold
        self.delta_epoch = delta_epoch

        self.rand_generator = np.random.default_rng(random_state)

        self.mean = data_train.mean()
        self.std = data_train.std().replace(0, 1)
        self.data_train = (data_train - self.mean) / self.std
        self.labels_train = labels_train.values

        unique_labels = np.unique(self.labels_train)
        if len(unique_labels) != 2 or not set(unique_labels).issubset({-1, 1}):
            self.labels_train = np.where(self.labels_train == unique_labels[0], -1, 1)

        self.n_samples, self.n_features = self.data_train.shape

        self.alpha = None
        self.support_vectors = None
        self.support_vector_labels = None
        self.support_vector_indices = None
        self._bias = 0.0

        self._kernel_cache = {}
        self._fitted = False

        self.support_vector_alphas = None
        self.data_train_full = None
        self.labels_train_full = None
        self.kernel_full = None

        if train:
            self.train()

    def _compute_kernel_matrix(self) -> np.ndarray:
        data = self.data_train.values

        if isinstance(self.kernel, RBFKernel):
            sq = np.sum(data * data, axis=1)
            dists = sq[:, None] + sq[None, :] - 2 * (data @ data.T)
            return np.exp(-self.kernel.gamma * dists)

        if isinstance(self.kernel, LinearKernel):
            return data @ data.T

        if isinstance(self.kernel, PolynomialKernel):
            gram = data @ data.T
            return (self.kernel.gamma * gram + self.kernel.coef0) ** self.kernel.degree

        kernel_matrix = np.zeros((self.n_samples, self.n_samples))

        for i in range(self.n_samples):
            for j in range(i, self.n_samples):
                k_val = self.kernel(data[i], data[j])
                kernel_matrix[i, j] = k_val
                kernel_matrix[j, i] = k_val

        return kernel_matrix

    @lru_cache(MAX_CACHE)
    def _get_kernel_value(self, i: int, j: int) -> float:
        if (i, j) in self._kernel_cache:
            return self._kernel_cache[(i, j)]

        k_val = self.kernel(self.data_train.values[i], self.data_train.values[j])
        self._kernel_cache[(i, j)] = k_val
        self._kernel_cache[(j, i)] = k_val
        return k_val

    def _decision_function(self, data: np.ndarray) -> np.ndarray:
        if not self._fitted:
            raise ValueError("Модель ещё не обучена")

        sv_idx = self.support_vector_indices
        sv = self.data_train_full[sv_idx]
        alphas = self.support_vector_alphas
        sv_labels = self.support_vector_labels

        if isinstance(self.kernel, RBFKernel):
            xx = np.sum(data * data, axis=1)
            ss = np.sum(sv * sv, axis=1)
            dists = xx[:, None] + ss[None, :] - 2 * (data @ sv.T)
            kern = np.exp(-self.kernel.gamma * dists)

        elif isinstance(self.kernel, LinearKernel):
            kern = data @ sv.T

        elif isinstance(self.kernel, PolynomialKernel):
            G = data @ sv.T
            kern = (self.kernel.gamma * G + self.kernel.coef0) ** self.kernel.degree

        else:
            m = data.shape[0]
            kern = np.empty((m, sv.shape[0]))
            for i in range(m):
                kern[i, :] = np.array([self.kernel(data[i], sv[j]) for j in range(sv.shape[0])])

        weighted = alphas * sv_labels
        decisions = kern @ weighted - self._bias
        return decisions

    def train(self) -> None:
        self.alpha = np.zeros(self.n_samples)
        self._bias = 0
        data_train = self.data_train.values
        labels_train = self.labels_train

        kernel = self._compute_kernel_matrix()

        n_batches = (self.n_samples + self.batch_size - 1) // self.batch_size if self.batch_size else 1
        prev_loss = float('inf')

        for epoch in range(self.epochs):
            indices = self.rand_generator.permutation(self.n_samples)
            epoch_loss = 0

            for batch in range(n_batches):
                if self.batch_size:
                    start = batch * self.batch_size
                    end = min(start + self.batch_size, self.n_samples)
                    batch_idx = indices[start:end]
                else:
                    batch_idx = indices

                alphas_labels = self.alpha * labels_train
                decisions_batch = kernel[batch_idx] @ alphas_labels - self._bias

                labels_batch = labels_train[batch_idx]
                cond_less = labels_batch * decisions_batch < 1
                cond_more = labels_batch * decisions_batch > 1
                batch_grad = np.zeros_like(decisions_batch)
                batch_grad[cond_less] = -self.soft_const * labels_batch[cond_less]
                batch_grad[cond_more] = 0
                eq_mask = ~(cond_less | cond_more)
                batch_grad[eq_mask] = -self.soft_const * labels_batch[eq_mask] * 0.5

                learning_rate = self.learning_rate / (1 + epoch / self.delta_epoch)
                self.alpha[batch_idx] -= learning_rate * batch_grad
                self.alpha = np.clip(self.alpha, 0, self.soft_const)

                decisions = kernel @ (self.alpha * labels_train) - self._bias
                hinge_loss = np.maximum(0, 1 - labels_train * decisions)
                reg_loss = 0.5 * np.dot(self.alpha * labels_train, kernel @ (self.alpha * labels_train))
                batch_loss = np.mean(hinge_loss) + reg_loss
                epoch_loss += batch_loss

                self._bias = np.mean(labels_train - (kernel @ (self.alpha * labels_train)))

            epoch_loss /= n_batches
            if abs(prev_loss - epoch_loss) < self.min_tol:
                break

            prev_loss = epoch_loss

        support_mask = self.alpha > self.sv_threshold
        self.support_vector_indices = np.flatnonzero(support_mask)

        self.support_vectors = data_train[self.support_vector_indices]
        self.support_vector_labels = labels_train[self.support_vector_indices]
        self.support_vector_alphas = self.alpha[self.support_vector_indices]

        self.data_train_full = data_train
        self.labels_train_full = labels_train
        self.kernel_full = kernel

        self._bias = 0
        for idx_in_sv, original_idx in enumerate(self.support_vector_indices):
            decision = np.sum(self.support_vector_alphas *
                              self.support_vector_labels *
                              kernel[original_idx][self.support_vector_indices]) - self._bias

            if float(self.support_vector_alphas[idx_in_sv]) < self.soft_const - self.sv_threshold:
                self._bias = decision
                break

        self._fitted = True

    def predict(self, data: pd.DataFrame) -> np.ndarray:
        if not self._fitted:
            raise ValueError("Модель не обучена")

        data = (data - self.mean) / self.std
        decisions = self._decision_function(data.values)
        return np.sign(decisions)

    def decision_function(self, data: pd.DataFrame) -> np.ndarray:
        if not self._fitted:
            raise ValueError("Модель не обучена")

        data = (data - self.mean) / self.std
        return self._decision_function(data.values)

    def accuracy(self, data: pd.DataFrame, labels: pd.Series) -> float:
        predictions = self.predict(data)

        unique_labels = np.unique(labels)
        if len(unique_labels) == 2:
            y_true = np.where(labels == unique_labels[0], -1, 1)
        else:
            y_true = labels.values

        return float(np.mean(predictions == y_true))
