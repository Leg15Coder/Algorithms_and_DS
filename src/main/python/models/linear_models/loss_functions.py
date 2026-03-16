from abc import ABC
from typing import Callable
import numpy as np


LOSS_T = Callable[[np.ndarray | float, np.ndarray | float], np.ndarray | float]


def _asarray_both(a, b):
    aw = np.asarray(a)
    bw = np.asarray(b)
    return aw, bw


def _maybe_scalar(res, a, b):
    arr = np.asarray(res)
    a_is_scalar = np.ndim(a) == 0
    b_is_scalar = np.ndim(b) == 0
    if a_is_scalar and b_is_scalar:
        try:
            return arr.item()
        except Exception as ex:
            print(ex)
            return arr
    return arr


def logistic_margin(weights: float | np.ndarray, targets: float | np.ndarray) -> float | np.ndarray:
    w, t = _asarray_both(weights, targets)
    res = w * t
    return _maybe_scalar(res, weights, targets)


def logistic_margin_with_norm(weights: float | np.ndarray, targets: float | np.ndarray) -> float | np.ndarray:
    w, t = _asarray_both(weights, targets)
    res = (w - 0.5) * (t - 0.5) * 4
    return _maybe_scalar(res, weights, targets)


def diff_margin(weights: float | np.ndarray, targets: float | np.ndarray) -> float | np.ndarray:
    w, t = _asarray_both(weights, targets)
    res = w - t
    return _maybe_scalar(res, weights, targets)


class BaseLoss(ABC):
    """Базовый класс для всех функций потерь"""
    def __init__(self, margin: Callable = logistic_margin):
        self.margin = margin

    def __call__(self, weights: float | np.ndarray, targets: float | np.ndarray) -> float | np.ndarray:
        raise NotImplementedError

    def der(self, weights: float | np.ndarray, targets: float | np.ndarray) -> float | np.ndarray:
        raise NotImplementedError


class IndicatorLoss(BaseLoss):
    def __call__(self, weights: float | np.ndarray, targets: float | np.ndarray) -> float | np.ndarray:
        w, t = _asarray_both(weights, targets)
        m = self.margin(w, t)
        res = np.where(m <= 0, 1.0, 0.0)
        return _maybe_scalar(res, weights, targets)

    def der(self, weights: float | np.ndarray, targets: float | np.ndarray) -> float | np.ndarray:
        w, t = _asarray_both(weights, targets)
        res = np.zeros_like(w, dtype=float)
        return _maybe_scalar(res, weights, targets)

    @staticmethod
    def __name__() -> str:
        return "IndicatorLoss"


class PerceptronLoss(BaseLoss):
    def __call__(self, weights: float | np.ndarray, targets: float | np.ndarray) -> float | np.ndarray:
        w, t = _asarray_both(weights, targets)
        m = self.margin(w, t)
        res = np.maximum(0.0, -m)
        return _maybe_scalar(res, weights, targets)

    def der(self, weights: float | np.ndarray, targets: float | np.ndarray) -> float | np.ndarray:
        w, t = _asarray_both(weights, targets)
        m = self.margin(w, t)
        res = np.where(m < 0, -1.0, 0.0)
        return _maybe_scalar(res, weights, targets)

    @staticmethod
    def __name__() -> str:
        return "PerceptronLoss"


class HingeLoss(BaseLoss):
    def __call__(self, weights: float | np.ndarray, targets: float | np.ndarray) -> float | np.ndarray:
        w, t = _asarray_both(weights, targets)
        m = self.margin(w, t)
        res = np.maximum(0.0, 1.0 - m)
        return _maybe_scalar(res, weights, targets)

    def der(self, weights: float | np.ndarray, targets: float | np.ndarray) -> float | np.ndarray:
        w, t = _asarray_both(weights, targets)
        m = self.margin(w, t)
        res = np.where(m < 1.0, -1.0, 0.0)
        return _maybe_scalar(res, weights, targets)

    @staticmethod
    def __name__() -> str:
        return "HingeLoss"


class LogLoss(BaseLoss):
    def __call__(self, weights: float | np.ndarray, targets: float | np.ndarray) -> float | np.ndarray:
        w, t = _asarray_both(weights, targets)
        m = self.margin(w, t)
        res = np.log2(1.0 + np.exp(-m))
        return _maybe_scalar(res, weights, targets)

    def der(self, weights: float | np.ndarray, targets: float | np.ndarray) -> float | np.ndarray:
        w, t = _asarray_both(weights, targets)
        m = self.margin(w, t)
        res = -1.0 / (1.0 + np.exp(m))
        return _maybe_scalar(res, weights, targets)

    @staticmethod
    def __name__() -> str:
        return "LogLoss"


class ExpLoss(BaseLoss):
    def __call__(self, weights: float | np.ndarray, targets: float | np.ndarray) -> float | np.ndarray:
        w, t = _asarray_both(weights, targets)
        m = self.margin(w, t)
        res = np.exp(-m)
        return _maybe_scalar(res, weights, targets)

    def der(self, weights: float | np.ndarray, targets: float | np.ndarray) -> float | np.ndarray:
        val = self.__call__(weights, targets)
        if np.ndim(val) == 0:
            return -float(val)
        return -np.asarray(val)

    @staticmethod
    def __name__() -> str:
        return "ExpLoss"


class MSELoss(BaseLoss):
    margin = diff_margin

    def __call__(self, weights: float | np.ndarray, targets: float | np.ndarray) -> float | np.ndarray:
        w, t = _asarray_both(weights, targets)
        m = self.margin(w, t)
        res = m ** 2
        return _maybe_scalar(res, weights, targets)

    def der(self, weights: float | np.ndarray, targets: float | np.ndarray) -> float | np.ndarray:
        w, t = _asarray_both(weights, targets)
        m = self.margin(w, t)
        res = 2.0 * m
        return _maybe_scalar(res, weights, targets)

    @staticmethod
    def __name__() -> str:
        return "MSELoss"


class MAELoss(BaseLoss):
    margin = diff_margin

    def __call__(self, weights: float | np.ndarray, targets: float | np.ndarray) -> float | np.ndarray:
        w, t = _asarray_both(weights, targets)
        m = self.margin(w, t)
        res = np.abs(m)
        return _maybe_scalar(res, weights, targets)

    def der(self, weights: float | np.ndarray, targets: float | np.ndarray) -> float | np.ndarray:
        w, t = _asarray_both(weights, targets)
        m = self.margin(w, t)
        res = np.sign(m)
        return _maybe_scalar(res, weights, targets)

    @staticmethod
    def __name__() -> str:
        return "MAELoss"


class BinaryCrossEntropy(BaseLoss):
    eps = 1e-15

    def __call__(self, weights: float | np.ndarray, label: float | np.ndarray) -> float | np.ndarray:
        w, l = _asarray_both(weights, label)
        w_clipped = np.clip(w, self.eps, 1.0 - self.eps)
        res = -(l * np.log(w_clipped) + (1.0 - l) * np.log(1.0 - w_clipped))
        return _maybe_scalar(res, weights, label)

    def der(self, weights: float | np.ndarray, label: float | np.ndarray) -> float | np.ndarray:
        w, l = _asarray_both(weights, label)
        w_clipped = np.clip(w, self.eps, 1.0 - self.eps)
        res = (w_clipped - l) / (w_clipped * (1.0 - w_clipped))
        return _maybe_scalar(res, weights, label)

    @staticmethod
    def __name__() -> str:
        return "BinaryCrossEntropy"


bce_loss = BinaryCrossEntropy()
indicator_loss = IndicatorLoss()
perceptron_loss = PerceptronLoss()
hinge_loss = HingeLoss()
log_loss = LogLoss()
exp_loss = ExpLoss()
mse_loss = MSELoss()
mae_loss = MAELoss()
