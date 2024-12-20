import java.util.Scanner;

public class Main {
  public static <T extends Comparable<T>> T max(T l, T r) {
    if (l == null) {
      return r;
    }
    return (l.compareTo(r) > 0) ? l : r;
  }

  public record Wrapper(Integer dp, Integer x, Integer y) implements Comparable<Wrapper> {
    @Override
    public int compareTo(Wrapper o) {
      return this.dp.compareTo(o.dp);
    }
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    int n = input.nextInt();
    int[] a1 = new int[n + 1];
    for (int i = 1; i <= n; ++i) {
      a1[i] = input.nextInt();
    }

    int m = input.nextInt();
    int[] a2 = new int[m + 1];
    for (int i = 1; i <= m; ++i) {
      a2[i] = input.nextInt();
    }
    Wrapper[][] dp = new Wrapper[n + 1][m + 1];
    Wrapper res = new Wrapper(0, 0, 0);

    for (int i = 0; i <= n; ++i) {
      dp[i][0] = new Wrapper(0, 0, 0);
    }
    for (int i = 0; i <= m; ++i) {
      dp[0][i] = new Wrapper(0, 0, 0);
    }

    for (int i = 1; i <= n; ++i) {
      for (int j = 1; j <= m; ++j) {
        if (a1[i] == a2[j]) {
          dp[i][j] = new Wrapper(dp[i - 1][j - 1].dp + 1, i - 1, j - 1);
          res = max(res, new Wrapper(dp[i][j].dp, i, j));
        } else {
          if (dp[i - 1][j].dp > dp[i][j - 1].dp) {
            dp[i][j] = new Wrapper(dp[i - 1][j].dp, i - 1, j);
          } else {
            dp[i][j] = new Wrapper(dp[i][j - 1].dp, i, j - 1);
          }
        }
      }
    }

    Integer[] ans = new Integer[res.dp];
    int count = res.dp;
    while (res.x != 0 && res.y != 0) {
      if (a1[res.x] == a2[res.y]) {
        ans[--count] = a1[res.x];
      }
      res = dp[res.x][res.y];
    }

    for (var e : ans) {
      System.out.print(e + " ");
    }
  }
}
