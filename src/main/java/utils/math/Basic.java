package utils.math;

public class Basic {
  public static long abs(long n) {
    return n > 0 ? n : -n;
  }

  public static boolean isPowerOfTwo(long n) {
    return (n & (n - 1)) == 0;
  }

  public static long pow(long base, long power) {
    if (base == 1 || base == 0) {
      return base;
    }

    if (base == -1) {
      return power % 2 == 0 ? 1 : -1;
    }

    if (power < 0) {
      throw new IllegalArgumentException("Функция работает только в целых числах");
    }

    if (isPowerOfTwo(base)) {
      while (base > 1) {
        base >>= 1;
        power <<= 1;
      }
      return base << power;
    }

    if (isPowerOfTwo(-base)) {
      base = -base;
      while (base > 1) {
        base >>= 1;
        power <<= 1;
      }

      long sign = power % 2 == 0 ? 1 : -1;
      return sign * (base << power);
    }

    if (power == 0) {
      return 1L;
    }

    if (power % 2 == 0) {
      return pow(base * base, power / 2);
    }

    return base * pow(base, power - 1);
  }

  public static long mod(long n, long module) {
    return ((n % module) + module) % module;
  }

  public static long mulMod(long a, long b, long module) {
    return mod(mod(a, module) * mod(b, module), module);
  }

  public static long sumMod(long a, long b, long module) {
    return mod(mod(a, module) + mod(b, module), module);
  }
}
