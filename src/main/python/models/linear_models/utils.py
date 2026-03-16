import math
import time
from typing import Callable

import numpy as np
import pandas as pd


class StopTimer:
    def __init__(self, max_time: float, step_update: Callable[[float], bool] = None):
        self.max_time = max_time
        self.step_update = step_update
        self.start_time = None
        self.is_running = False

    def __call__(self, iteration: int, step: float) -> float:  # True - остановка
        if not self.is_running or iteration < 1:
            self.is_running = True
            self.start_time = time.time()
            return False
        elif time.time() - self.start_time > self.max_time:
            self.is_running = False
            return True
        return self.step_update(step) if self.step_update else False


def sign(x: float) -> int:
    return 1 if x > 0 else -1 if x < 0 else 0


class ExpStepUpdate:
    def __init__(self, initial_step: float = 1e-3, size: int = 1_000_000):
        self.initial_step = initial_step
        self.size = size

    def __call__(self, x: pd.DataFrame, w : np.ndarray, s : float, k : int) -> float:
        return self.initial_step * math.exp(- k / self.size)


class ExpStepUpdate:
    def __init__(self, initial_step: float = 1e-3, size: int = 1_000_000):
        self.initial_step = initial_step
        self.size = size

    def __call__(self, x: pd.DataFrame, w : np.ndarray, s : float, k : int) -> float:
        return self.initial_step * math.exp(- k / self.size)


class LinStepUpdate:
    def __init__(self, initial_step: float = 1e-3, size: int = 1_000_000):
        self.initial_step = initial_step
        self.size = size

    def __call__(self, x: pd.DataFrame, w : np.ndarray, s : float, k : int) -> float:
        return self.initial_step * (1 - k / self.size)


timer = lambda t: StopTimer(t)
exp_step_update = lambda ist, sz: ExpStepUpdate(ist, sz)
lin_step_update = lambda ist, sz: LinStepUpdate(ist, sz)
