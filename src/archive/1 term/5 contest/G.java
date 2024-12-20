import java.util.Scanner;

public class Main {
  public static <T extends Comparable<T>> T max(T l, T r) {
    if (l == null) {
      return r;
    }
    return (l.compareTo(r) > 0) ? l : r;
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    int n = input.nextInt();
    int m = input.nextInt();

    long[] a1 = new long[n + 1];
    for (int i = 1; i <= n; ++i) {
      a1[i] = input.nextLong();
    }

    long[] a2 = new long[m + 1];
    for (int i = 1; i <= m; ++i) {
      a2[i] = input.nextLong();
    }

    Integer[][] dp = new Integer[n + 1][m + 1];

    for (int i = 0; i <= n; ++i) {
      dp[i][0] = 0;
    }
    for (int i = 0; i <= m; ++i) {
      dp[0][i] = 0;
    }

    for (int i = 1; i <= n; ++i) {
      int mx = 0;
      for (int j = 1; j <= m; ++j) {
        dp[i][j] = dp[i - 1][j];

        if (a1[i] == a2[j] && mx + 1 > dp[i][j]) {
          dp[i][j] = mx + 1;
        }

        if (a2[j] < a1[i]) {
          mx = max(mx, dp[i][j]);
        }
      }
    }

    for (int i = 0; i < m; ++i) {
      dp[n][m] = max(dp[n][m], dp[n][i]);
    }
    System.out.println(dp[n][m]);
  }
}
