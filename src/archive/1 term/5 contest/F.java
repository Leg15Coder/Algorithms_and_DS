import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class Main {
  static Map<Pair<Integer, Integer>, Double> cache = new HashMap<>();

  public static <T extends Comparable<T>> T max(T l, T r) {
    if (l == null) {
      return r;
    }
    return (l.compareTo(r) > 0) ? l : r;
  }

  public static class Pair<T, E> {
    public T first;
    public E second;

    public Pair(T first, E second) {
      this.first = first;
      this.second = second;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Pair<?, ?> pair = (Pair<?, ?>) o;
      return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
      return Objects.hash(first, second);
    }
  }

  public static Double func(Pair<Integer, Integer> range, Double[] arr) {
    if (cache.containsKey(range)) {
      return cache.get(range);
    }

    if (range.second - range.first <= 1) {
      if (range.second - range.first == 1) {
        return (arr[range.first] + arr[range.second]) / 2;
      } else if (range.second - range.first == 0) {
        return arr[range.first];
      }
      return 0D;
    }

    Double result = 0D;

    for (int i = range.first; i < range.second; ++i) {
      var left = new Pair<>(range.first, i);
      var right = new Pair<>(i + 1, range.second);
      result = max(result, (func(left, arr) + func(right, arr)) / 2);
    }

    cache.put(range, result);
    return result;
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    int n = input.nextInt();

    Double[] arr = new Double[n];
    for (int i = 0; i < n; ++i) {
      arr[i] = input.nextDouble();
    }

    System.out.println(func(new Pair<>(0, n - 1), arr));
  }
}
