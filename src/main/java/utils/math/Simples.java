package utils.math;

import structures.arrays.ArrayList;

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
}
