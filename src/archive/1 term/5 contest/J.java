import static java.util.Arrays.fill;

import java.util.Scanner;

public class Main {
  public static int checkProfs(int left, int right, int n) {
    for (int i = 1; i < n; ++i) {
      boolean b1 = (left & (1 << i)) == 0;
      boolean b2 = (left & (1 << (i - 1))) == 0;
      boolean b3 = (right & (1 << i)) == 0;
      boolean b4 = (right & (1 << (i - 1))) == 0;
      if (b1 == b2 && b2 == b3 && b3 == b4) {
        return 0;
      }
    }
    return 1;
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    int m = input.nextInt();
    int n = input.nextInt();

    if (n > m) {
      int tmp = n;
      n = m;
      m = tmp;
    }

    int[][] profiles = new int[1 << n][1 << n];
    Long[][] dp = new Long[m][1 << n];

    for (int i = 0; i < (1 << n); ++i) {
      for (int j = 0; j < (1 << n); ++j) {
        profiles[i][j] = checkProfs(i, j, n);
      }
    }

    fill(dp[0], 1L);
    for (int i = 1; i < m; ++i) {
      fill(dp[i], 0L);
    }

    for (int k = 1; k < m; ++k) {
      for (int i = 0; i < (1 << n); ++i) {
        for (int j = 0; j < (1 << n); ++j) {
          dp[k][i] += dp[k - 1][j] * profiles[j][i];
        }
      }
    }

    Long result = 0L;
    for (int i = 0; i < (1 << n); ++i) {
      result += dp[m - 1][i];
    }

    System.out.println(result);
  }
}
