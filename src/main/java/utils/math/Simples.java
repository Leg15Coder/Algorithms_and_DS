package utils.math;

import structures.arrays.ArrayList;
import structures.common.Pair;

import static utils.math.Basic.abs;
import static utils.math.Basic.mod;

public class Simples {
  public static ArrayList<Integer> EratosthenesSieve(int end) {
    return EratosthenesSieve(end, true);
  }

  public static ArrayList<Integer> EratosthenesSieve(int end, boolean isSimple) {
    ArrayList<Integer> result = new ArrayList<>();
    boolean[] isNotSimple = new boolean[end + 1];
    isNotSimple[0] = isNotSimple[1] = true;

    for (int i = 2; i <= end; ++i) {
      if (!isNotSimple[i]) {
        if (i * i <= end) {
          for (int j = i * i; j <= end; j+=i) {
            isNotSimple[j] = true;
          }
        }
      }
    }

    for (int i = 1; i <= end; ++i) {
      if (isNotSimple[i] != isSimple) {
        result.insert(i);
      }
    }

    return result;
  }

  public static ArrayList<Integer> linearEratosthenesSieve(int end) {
    ArrayList<Integer> result = new ArrayList<>();
    int[] minimalDivisors = new int[end + 1];

    for (int i = 2; i <= end; ++i) {
      if (minimalDivisors[i] == 0) {
        result.insert(i);
        minimalDivisors[i] = i;
      }

      for (int j = 0; j < result.getSize() && result.getAt(j) <= minimalDivisors[i] && i * result.getAt(j) <= end; ++j) {
        minimalDivisors[i * result.getAt(j)] = result.getAt(j);
      }
    }

    return result;
  }

  public static boolean isSimple(int n) {
    if (n < 2) {
      return false;
    }
    if (n == 2) {
      return true;
    }
    if (n % 2 == 0) {
      return false;
    }

    int cur = 3;
    while (cur * cur <= n) {
      if (n % cur == 0) {
        return false;
      }

      cur += 2;
    }

    return true;
  }

  public static long gcd(long a, long b) {
    if (a == 0 || b == 0) {
      throw new IllegalArgumentException("Нет делителей нуля");
    }

    if (abs(a) == 1 || abs(b) == 1) {
      return 1L;
    }

    while (a != b) {
      if (a > b) {
        a %= b;
      } else {
        b %= a;
      }
    }

    return a;
  }

  public static long gcd(long[] numbers) {
    if (numbers.length == 0) {
      throw new IllegalArgumentException("Нельзя найти НОД пустого массива чисел");
    }

    if (numbers.length == 1) {
      return numbers[0];
    }

    for (int i = 1; i < numbers.length; ++i) {
      numbers[i] = gcd(numbers[i - 1], numbers[i]);
    }

    return numbers[numbers.length - 1];
  }

  public static long gcd(ArrayList<Long> numbers) {
    if (numbers.getSize() == 0) {
      throw new IllegalArgumentException("Нельзя найти НОД пустого массива чисел");
    }

    if (numbers.getSize() == 1) {
      return numbers.first();
    }

    for (int i = 1; i < numbers.getSize(); ++i) {
      numbers.setAt(i, gcd(numbers.getAt(i - 1), numbers.getAt(i)));
    }

    return numbers.last();
  }

  public static long lcm(long a, long b) {
    return a * b / gcd(a, b);
  }

  public static long lcm(long[] numbers) {
    if (numbers.length == 0) {
      throw new IllegalArgumentException("Нельзя найти НОК пустого массива чисел");
    }

    if (numbers.length == 1) {
      return numbers[0];
    }

    for (int i = 1; i < numbers.length; ++i) {
      numbers[i] = lcm(numbers[i - 1], numbers[i]);
    }

    return numbers[numbers.length - 1];
  }

  public static long lcm(ArrayList<Long> numbers) {
    if (numbers.getSize() == 0) {
      throw new IllegalArgumentException("Нельзя найти НОК пустого массива чисел");
    }

    if (numbers.getSize() == 1) {
      return numbers.first();
    }

    for (int i = 1; i < numbers.getSize(); ++i) {
      numbers.setAt(i, lcm(numbers.getAt(i - 1), numbers.getAt(i)));
    }

    return numbers.last();
  }

  public static long inverseByModulo(long n, long module) {
    if (n < 0 || module < 0) {
      throw new IllegalArgumentException("Числа не могут быть отрицательны");
    }
    if (module >= n) {
      throw new IllegalArgumentException("Модуль не может быть больше или равен числу");
    }

    return mod(extendedEuclid(n, module).second.first, module); // todo check is real
  }

  private static Pair<Long, Pair<Long, Long>> extendedEuclid(long a, long b) {
    if (b == 0) {
      return new Pair<>(
          a,
          new Pair<>(1L, 0L)
      );
    }
    var pair = extendedEuclid(b, a % b);
    return new Pair<>(
        pair.first,
        new Pair<>(
            pair.second.second,
            (pair.second.first - a) / b
        )
    );
  }
}
