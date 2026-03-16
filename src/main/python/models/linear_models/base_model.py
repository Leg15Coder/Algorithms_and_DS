from abc import ABC, abstractmethod

import numpy as np
import pandas as pd


class Model(ABC):
    @abstractmethod
    def train(self) -> None:
        pass

    @abstractmethod
    def predict(self, data: pd.DataFrame) -> np.ndarray:
        pass

    @abstractmethod
    def accuracy(self, data: pd.DataFrame, labels: pd.Series) -> float:
        pass
