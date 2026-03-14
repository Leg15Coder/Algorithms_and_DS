from abc import ABC, abstractmethod
from typing import Callable

import numpy as np
from src.main.python.models.linear_models.utils import sign


class Regularizer(ABC, Callable[[np.ndarray, float], float]):
    @abstractmethod
    def der(self, weights: np.ndarray, bias: float) -> tuple[np.ndarray, float]:
        pass

    @abstractmethod
    def scalar_der(self, weights: np.ndarray, bias: float) -> float:
        pass


class EmptyRegularizer(Regularizer):
    def __call__(self, weights: np.ndarray, bias: float) -> float:
        return 0

    def der(self, weights: np.ndarray, bias: float) -> tuple[np.ndarray, float]:
        return np.zeros(weights.shape), 0

    def scalar_der(self, weights: np.ndarray, bias: float) -> float:
        return 0


class L1Regularizer(Regularizer):
    def __init__(self, alpha: float = 1e-3):
        self.alpha = alpha

    def __call__(self, weights: np.ndarray, bias: float) -> float:
        return self.alpha * (np.abs(weights).sum() + bias)

    def der(self, weights: np.ndarray, bias: float) -> tuple[np.ndarray, float]:
        return self.alpha * np.vectorize(sign)(weights), self.alpha * sign(bias)

    def scalar_der(self, weights: np.ndarray, bias: float) -> float:
        return self.alpha * ((np.vectorize(sign)(weights)).sum() + sign(bias))


class L2Regularizer(Regularizer):
    def __init__(self, alpha: float = 1e-3):
        self.alpha = alpha

    def __call__(self, weights: np.ndarray, bias: float) -> float:
        return self.alpha * ((weights ** 2).sum() + bias ** 2)

    def der(self, weights: np.ndarray, bias: float) -> tuple[np.ndarray, float]:
        return 2 * self.alpha * weights, 2 * self.alpha * bias

    def scalar_der(self, weights: np.ndarray, bias: float) -> float:
        return 2 * self.alpha * (weights.sum() + bias)


class L2NormRegularizer(Regularizer):
    def __init__(self, alpha: float = 1e-3):
        self.alpha = alpha

    def __call__(self, weights: np.ndarray, bias: float) -> float:
        return self.alpha * ((weights ** 2).sum() + bias ** 2) ** 0.5

    def der(self, weights: np.ndarray, bias: float) -> tuple[np.ndarray, float]:
        return (self.alpha ** 2) * weights / self(weights, bias), (self.alpha ** 2) * bias / self(weights, bias)

    def scalar_der(self, weights: np.ndarray, bias: float) -> float:
        return (self.alpha ** 2) * (weights.sum() + bias) / self(weights, bias)
