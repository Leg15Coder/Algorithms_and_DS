import java.math.BigInteger;
import java.util.Scanner;

public class Main {
  public static long checkProfs(int left, int right, int n) {
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

  public static class Pair<T, E> {
    public T first;
    public E second;

    public Pair(T first, E second) {
      this.first = first;
      this.second = second;
    }
  }

  public static class Matrix {
    private final Long[][] matrix;

    public Matrix(Long[][] matrix) {
      this.matrix = matrix;
    }

    public Long get(int x, int y) {
      return matrix[x][y];
    }

    public Long sum(long mod) {
      long result = 0;
      for (var row : matrix) {
        for (var e : row) {
          result += e;
          result %= mod;
        }
      }
      return result;
    }

    public Matrix neutral() {
      var newMatrix = new Long[matrix.length][matrix[0].length];
      for (int i = 0; i < matrix.length; ++i) {
        for (int j = 0; j < matrix[0].length; ++j) {
          newMatrix[i][j] = (long) (i == j ? 1 : 0);
        }
      }
      return new Matrix(newMatrix);
    }

    public Pair<Integer, Integer> getSize() {
      return new Pair<>(matrix.length, matrix[0].length);
    }

    @Override
    public String toString() {
      StringBuilder result = new StringBuilder();
      for (var row : matrix) {
        for (var e : row) {
          result.append(e).append(" ");
        }
        result.append("\n");
      }
      return result.toString();
    }
  }

  public static Matrix mul(Matrix l, Matrix r, long mod) throws Exception {
    var lSize = l.getSize();
    var rSize = r.getSize();
    if (!lSize.second.equals(rSize.first)) {
      throw new Exception("Неправильный размер матрицы");
    }
    Long[][] res = new Long[lSize.first][rSize.second];
    for (int i = 0; i < lSize.first; ++i) {
      for (int j = 0; j < rSize.second; ++j) {
        long cur = 0;
        for (int k = 0; k < lSize.second; ++k) {
          cur += l.get(i, k) * r.get(k, j);
          cur %= mod;
        }
        res[i][j] = cur;
      }
    }

    return new Matrix(res);
  }

  public static Matrix pow(Matrix m, BigInteger pow, long mod) throws Exception {
    if (pow.equals(new BigInteger("0"))) {
      return m.neutral();
    }
    if (pow.equals(new BigInteger("1"))) {
      return m;
    }
    var tmp = pow(m, pow.divide(new BigInteger("2")), mod);
    if (pow.mod(new BigInteger("2")).equals(new BigInteger("0"))) {
      return mul(tmp, tmp, mod);
    }
    return mul(m, mul(tmp, tmp, mod), mod);
  }

  public static void main(String[] args) throws Exception {
    Scanner input = new Scanner(System.in);

    BigInteger n = new BigInteger(input.next());
    int m = input.nextInt();
    long mod = input.nextLong();

    Long[][] profiles = new Long[1 << m][1 << m];
    for (int i = 0; i < (1 << m); ++i) {
      for (int j = 0; j < (1 << m); ++j) {
        profiles[i][j] = checkProfs(i, j, m);
      }
    }

    Matrix matrix = new Matrix(profiles);
    // System.out.println(matrix);
    matrix = pow(matrix, n.subtract(new BigInteger("1")), mod);
    // System.out.println(matrix);

    Long[][] fin = new Long[1 << m][1];
    for (int i = 0; i < (1 << m); ++i) {
      fin[i][0] = 1L;
    }

    matrix = mul(matrix, new Matrix(fin), mod);
    // System.out.println(matrix);

    System.out.println(matrix.sum(mod));
  }
}
