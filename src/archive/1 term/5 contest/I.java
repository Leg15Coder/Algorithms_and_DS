import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static <T extends Comparable<T>> T min(T l, T r) {
    if (l == null) {
      return r;
    }
    return (l.compareTo(r) > 0) ? r : l;
  }

  public static <T extends Comparable<T>> T max(T l, T r) {
    if (l == null) {
      return r;
    }
    return (l.compareTo(r) > 0) ? l : r;
  }

  public static class Wrapper implements Comparable<Wrapper> {
    public final Integer value;
    public List<Integer> path;

    public Wrapper(Integer value, List<Integer> path) {
      this.value = value;
      this.path = path;
    }

    @Override
    public int compareTo(Wrapper o) {
      return this.value.compareTo(o.value);
    }

    @Override
    public String toString() {
      return value + "";
    }
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    int n = input.nextInt();

    Integer[][] roads = new Integer[n][n];
    Wrapper[][] dp = new Wrapper[1 << n][n];

    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        dp[1 << i][j] = new Wrapper(0, List.of(new Integer[] {i + 1}));
        roads[i][j] = input.nextInt();
      }
    }

    for (int i = 1; i < (1 << n); ++i) {
      for (int j = 0; j < n; ++j) {
        if ((i & (1 << j)) != 0) {
          for (int k = 0; k < n; ++k) {
            if ((i & (1 << k)) == 0 && j != k) {
              Wrapper newValue = dp[i][j];

              var arr = new ArrayList<>(newValue.path);
              arr.add(k + 1);

              newValue = new Wrapper(newValue.value + roads[j][k], arr);

              dp[(i | (1 << k))][k] = min(dp[(i | (1 << k))][k], newValue);
            }
          }
        }
      }
    }

    Wrapper result = null;
    for (int i = 0; i < n; ++i) {
      result = min(result, dp[(1 << n) - 1][i]);
    }

    System.out.println(result.value);
    for (int i = 0; i < result.path.size(); ++i) {
      System.out.print(result.path.get(i) + " ");
    }
  }
}
