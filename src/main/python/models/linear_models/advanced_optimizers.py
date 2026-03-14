from typing import Any

import numpy as np

from src.main.python.models.linear_models.linear import LinearModel


class OptimizedGD(LinearModel):
    def __init__(self, eps: float = 1e-8, **kwargs: Any):
        self.eps = eps
        super().__init__(**kwargs)


class RMSProp(OptimizedGD):  # Running mean square (temperature)
    def __init__(self,
                 momentum_rate: float = 0.9,
                 train: bool = False,
                 **kwargs: Any):
        super().__init__(train=False, **kwargs)
        self.momentum_rate = momentum_rate
        if train:
            self.train()

    def train(self) -> None:
        self._weights = self.generator.normal(0, 0.1, self.size[1])
        self._bias = 0.0
        step = self.initial_step
        i = 0
        n_samples = self.size[0]
        velocity = np.zeros_like(self._weights)
        _b_vel = 0.0

        while not self.stop_rule(i, step):
            if self.batch_size == 1:
                index = self.generator.integers(0, n_samples)
                batch = self.data_train[index:index+1]
                batch_label = np.array([self.labels_train[index]])
            else:
                index = self.generator.integers(0, n_samples, size=self.batch_size)
                batch = self.data_train[index]
                batch_label = self.labels_train[index]

            grad_vals = np.asarray(self._der_loss_func(float(self._f(batch)), batch_label))
            grad_vals = np.clip(grad_vals, -self.max_gradient, self.max_gradient)

            grads = (grad_vals.reshape(-1, 1) * batch)
            gradient = grads.mean(axis=0) - self.regularizer.der(self._weights, self._bias)[0]
            _b_grad = (grad_vals - self.regularizer.der(self._weights, self._bias)[1]).mean()

            velocity = self.momentum_rate * velocity + (1 - self.momentum_rate) * (gradient ** 2)
            _b_vel = self.momentum_rate * _b_vel + (1 - self.momentum_rate) * (_b_grad ** 2)

            self._weights -= step * gradient / (np.sqrt(velocity) + self.eps)
            self._bias -= step * _b_grad / (np.sqrt(_b_vel) + self.eps)

            step = self.step_update_rule(self.data_train, self._weights, step, i)
            i += 1


class AdaDelta(OptimizedGD):  # Adaptive learning rate
    def __init__(self,
                 momentum_rate: float = 0.95,
                 train: bool = False,
                 **kwargs: Any):
        super().__init__(train=False, **kwargs)
        self.momentum_rate = momentum_rate

        if train:
            self.train()

    def train(self) -> None:
        self._weights = self.generator.normal(0, 0.1, self.size[1])
        self._bias = 0.0
        i = 0
        n_samples = self.size[0]

        gradient_temperature = np.zeros_like(self._weights)
        gradient_mass = np.zeros_like(self._weights)
        _b_temp = 0.0
        _b_mass = 0.0

        while not self.stop_rule(i, self.initial_step):
            if self.batch_size == 1:
                index = self.generator.integers(0, n_samples)
                batch = self.data_train[index:index+1]
                batch_labels = np.array([self.labels_train[index]])
            else:
                index = self.generator.integers(0, n_samples, size=self.batch_size)
                batch = self.data_train[index]
                batch_labels = self.labels_train[index]

            grad_vals = np.asarray(self._der_loss_func(float(self._f(batch)), batch_labels))
            grad_vals = np.clip(grad_vals, -self.max_gradient, self.max_gradient)

            grads = (grad_vals.reshape(-1, 1) * batch)
            gradient = grads.mean(axis=0) - self.regularizer.der(self._weights, self._bias)[0]
            _b_grad = (grad_vals - self.regularizer.der(self._weights, self._bias)[1]).mean()

            gradient_temperature = self.momentum_rate * gradient_temperature + (1 - self.momentum_rate) * (gradient ** 2)
            _b_temp = self.momentum_rate * _b_temp + (1 - self.momentum_rate) * (_b_grad ** 2)

            rms_delta = np.sqrt(gradient_mass + self.eps)
            rms_grad = np.sqrt(gradient_temperature + self.eps)
            delta = - (rms_delta / rms_grad) * gradient
            _b_delta = - (np.sqrt(_b_mass + self.eps) / np.sqrt(_b_temp + self.eps)) * _b_grad if _b_temp > 0 else -_b_grad

            self._weights += delta
            self._bias += _b_delta

            gradient_mass = self.momentum_rate * gradient_mass + (1 - self.momentum_rate) * (delta ** 2)
            _b_mass = self.momentum_rate * _b_mass + (1 - self.momentum_rate) * (_b_delta ** 2)
            i += 1


class Adam(OptimizedGD):  # Adaptive Moment Estimation
    def __init__(self,
                 inertia: float = 0.9,
                 inertia_std: float = 0.999,
                 train: bool = False,
                 **kwargs: Any):
        super().__init__(train=False, **kwargs)
        self.inertia = inertia
        self.inertia_std = inertia_std

        if train:
            self.train()

    def train(self) -> None:
        self._weights = self.generator.normal(0, 0.1, self.size[1])
        self._bias = 0.0
        step = self.initial_step
        mass = np.zeros_like(self._weights)
        velocity = np.zeros_like(self._weights)
        _b_mass = 0.0
        _b_vel = 0.0
        time = 0
        n_samples = self.size[0]

        while not self.stop_rule(time, step):
            if self.batch_size == 1:
                index = self.generator.integers(0, n_samples)
                batch = self.data_train[index:index+1]
                batch_labels = np.array([self.labels_train[index]])
            else:
                index = self.generator.integers(0, n_samples, size=self.batch_size)
                batch = self.data_train[index]
                batch_labels = self.labels_train[index]

            grad_vals = np.asarray(self._der_loss_func(float(self._f(batch)), batch_labels))
            grad_vals = np.clip(grad_vals, -self.max_gradient, self.max_gradient)

            grads = (grad_vals.reshape(-1, 1) * batch)
            gradient = grads.mean(axis=0) - self.regularizer.der(self._weights, self._bias)[0]
            _b_grad = (grad_vals - self.regularizer.der(self._weights, self._bias)[1]).mean()

            time += 1
            mass = self.inertia * mass + (1 - self.inertia) * gradient
            velocity = self.inertia_std * velocity + (1 - self.inertia_std) * (gradient ** 2)
            _b_mass = self.inertia * _b_mass + (1 - self.inertia) * _b_grad
            _b_vel = self.inertia_std * _b_vel + (1 - self.inertia_std) * (_b_grad ** 2)

            m_corr = mass / (1 - self.inertia ** time)
            v_corr = velocity / (1 - self.inertia_std ** time)
            mb_corr = _b_mass / (1 - self.inertia ** time)
            vb_corr = _b_vel / (1 - self.inertia_std ** time)

            self._weights -= step * m_corr / (np.sqrt(v_corr) + self.eps)
            self._bias -= step * mb_corr / (np.sqrt(vb_corr) + self.eps)

            step = self.step_update_rule(self.data_train, self._weights, step, time)


class NAdam(OptimizedGD):  # Nesterov-accelerated Adaptive Moment Estimation
    def __init__(self,
                 inertia: float = 0.9,
                 inertia_std: float = 0.999,
                 train: bool = False,
                 **kwargs: Any):
        super().__init__(train=False, **kwargs)
        self.inertia = inertia
        self.inertia_std = inertia_std

        if train:
            self.train()

    def train(self) -> None:
        self._weights = self.generator.normal(0, 0.1, self.size[1])
        self._bias = 0.0
        step = self.initial_step
        mass = np.zeros_like(self._weights)
        velocity = np.zeros_like(self._weights)
        _b_mass = 0.0
        _b_vel = 0.0
        time = 0
        n_samples = self.size[0]

        while not self.stop_rule(time, step):
            if self.batch_size == 1:
                index = self.generator.integers(0, n_samples)
                batch = self.data_train[index:index+1]
                batch_labels = np.array([self.labels_train[index]])
            else:
                index = self.generator.integers(0, n_samples, size=self.batch_size)
                batch = self.data_train[index]
                batch_labels = self.labels_train[index]

            grad_vals = np.asarray(self._der_loss_func(float(self._f(batch)), batch_labels))
            grad_vals = np.clip(grad_vals, -self.max_gradient, self.max_gradient)

            grads = (grad_vals.reshape(-1, 1) * batch)
            gradient = grads.mean(axis=0) - self.regularizer.der(self._weights, self._bias)[0]
            _b_grad = (grad_vals - self.regularizer.der(self._weights, self._bias)[1]).mean()

            time += 1
            mass = self.inertia * mass + (1 - self.inertia) * gradient
            velocity = self.inertia_std * velocity + (1 - self.inertia_std) * (gradient ** 2)
            _b_mass = self.inertia * _b_mass + (1 - self.inertia) * _b_grad
            _b_vel = self.inertia_std * _b_vel + (1 - self.inertia_std) * (_b_grad ** 2)

            m_corr = mass / (1 - self.inertia ** time)
            v_corr = velocity / (1 - self.inertia_std ** time)
            mb_corr = _b_mass / (1 - self.inertia ** time)
            vb_corr = _b_vel / (1 - self.inertia_std ** time)

            m_bar = self.inertia * m_corr + (1 - self.inertia) * gradient / (1 - self.inertia ** time)
            mb_bar = self.inertia * mb_corr + (1 - self.inertia) * _b_grad / (1 - self.inertia ** time)

            self._weights -= step * m_bar / (np.sqrt(v_corr) + self.eps)
            self._bias -= step * mb_bar / (np.sqrt(vb_corr) + self.eps)

            step = self.step_update_rule(self.data_train, self._weights, step, time)


class DiagMethod(OptimizedGD):
    def train(self) -> None:
        self._weights = self.generator.normal(0, 0.1, self.size[1])
        self._bias = 0.0
        step = self.initial_step
        i = 0
        n_samples = self.size[0]
        damp_parameter = 1e-3

        while not self.stop_rule(i, step):
            if self.batch_size == 1:
                index = self.generator.integers(0, n_samples)
                batch = self.data_train[index:index+1]
                batch_labels = np.array([self.labels_train[index]])
            else:
                index = self.generator.integers(0, n_samples, size=self.batch_size)
                batch = self.data_train[index]
                batch_labels = self.labels_train[index]

            predictions = self._f(batch)
            loss_vals = self.loss_func(predictions, batch_labels)
            loss_vals = np.asarray(loss_vals)
            loss_before = float(np.mean(loss_vals))

            der_vals = np.asarray(self._der_loss_func(predictions, batch_labels))
            der_vals = np.clip(der_vals, -self.max_gradient, self.max_gradient)

            grads = (der_vals.reshape(-1, 1) * batch)
            g = grads.mean(axis=0) - self.regularizer.der(self._weights, self._bias)[0]
            gb = (der_vals - self.regularizer.der(self._weights, self._bias)[1]).mean()

            hessian = np.maximum(np.mean((der_vals.reshape(-1, 1) * batch) ** 2, axis=0), self.eps)
            _b_hess = max(float(np.mean(der_vals ** 2)), self.eps)

            delta_w = - step * g / (hessian + damp_parameter)
            delta_b = - step * gb / (_b_hess + damp_parameter)

            w_cand = self._weights + delta_w
            b_cand = self._bias + delta_b

            predictions_cand = batch.dot(w_cand) + b_cand
            loss_c_vals = self.loss_func(predictions_cand, batch_labels)
            loss_c_vals = np.asarray(loss_c_vals)
            loss_after = float(np.mean(loss_c_vals))

            if loss_after < loss_before:
                self._weights = w_cand
                self._bias = b_cand
                damp_parameter = max(damp_parameter * 0.1, 1e-12)
            else:
                damp_parameter = min(damp_parameter * 10.0, 1e12)

            step = self.step_update_rule(self.data_train, self._weights, step, i)
            i += 1
