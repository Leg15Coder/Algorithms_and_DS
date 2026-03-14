from typing import Iterable

import numpy as np
import pandas as pd

from src.main.python.models.linear_models.linear import LinearModel, LinearClassifier


class KNNModel(LinearModel):
    def __init__(self, k: int = 5, **kwargs):
        super().__init__(**kwargs)
        self.k = k

    def train(self) -> None:  # Не вычисляется явно
        pass

    def predict(self, data: pd.DataFrame) -> np.ndarray:
        normalized = (data - self.mean) / self.std
        data = normalized.values
        n_train = self.data_train.shape[0]
        m_test = data.shape[0]

        x_test_sq = np.sum(data ** 2, axis=1).reshape(-1, 1)
        x_train_sq = np.sum(self.data_train ** 2, axis=1).reshape(1, -1)
        cross = data.dot(self.data_train.T)
        dists_sq = x_test_sq + x_train_sq - 2.0 * cross

        k = min(self.k, n_train)
        idx_part = np.argpartition(dists_sq, kth=k - 1, axis=1)[:, :k]

        row_indices = np.arange(m_test)[:, None]
        candidate_dists = dists_sq[row_indices, idx_part]
        order = np.argsort(candidate_dists, axis=1)
        idx_sorted = idx_part[row_indices, order]

        nearest_labels = self.labels_train[idx_sorted]

        predictions = []
        for row in nearest_labels:
            vals, counts = np.unique(row, return_counts=True)
            predictions.append(vals[np.argmax(counts)])

        return np.array(predictions, dtype=np.float64)


class KNNClassifier(LinearClassifier):
    def __init__(self, k: int = 5, classes_number: int = None, exact_classes: Iterable[float] = None, **kwargs):
        if classes_number is None and exact_classes is None:
            labels = kwargs.get('labels_train', None)
            if labels is not None:
                classes_number = np.unique(labels).shape[0]

        if classes_number is not None:
            kwargs['classes_number'] = classes_number
        if exact_classes is not None:
            kwargs['exact_classes'] = exact_classes

        super().__init__(**kwargs)

        self.k = k
        self._weights = None
        self._bias = None

    def train(self) -> None:  # Не вычисляется явно
        pass

    def predict(self, data: pd.DataFrame) -> np.ndarray:
        normalized = (data - self.mean) / self.std
        data = normalized.values

        x_test_sq = np.sum(data ** 2, axis=1).reshape(-1, 1)
        x_train_sq = np.sum(self.data_train ** 2, axis=1).reshape(1, -1)
        cross = data.dot(self.data_train.T)
        dists_sq = x_test_sq + x_train_sq - 2.0 * cross

        k = min(self.k, self.data_train.shape[0])
        idx_part = np.argpartition(dists_sq, kth=k - 1, axis=1)[:, :k]
        row_indices = np.arange(data.shape[0])[:, None]
        candidate_dists = dists_sq[row_indices, idx_part]
        order = np.argsort(candidate_dists, axis=1)
        idx_sorted = idx_part[row_indices, order]

        nearest_labels = self.labels_train[idx_sorted]

        predictions = []
        for row in nearest_labels:
            vals, counts = np.unique(row, return_counts=True)
            predictions.append(vals[np.argmax(counts)])

        return np.array(predictions)

    def accuracy(self, data: pd.DataFrame, labels: pd.Series) -> float:
        predictions = self.predict(data)
        return float(np.mean(predictions == labels))
