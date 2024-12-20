import static java.util.Arrays.sort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
  public static <T extends Comparable<T>> T max(T l, T r) {
    if (l == null & r == null) {
      return null;
    }
    if (l == null) {
      return r;
    }
    if (r == null) {
      return l;
    }
    return (l.compareTo(r) > 0) ? l : r;
  }

  public record Range(int left, int right) {

    public int length() {
      return right - left;
    }

    public int middle() {
      return left + length() / 2;
    }

    public boolean isInside(Range other) {
      return left <= other.left() && other.right() <= right;
    }

    public boolean isOutside(Range other) {
      return left >= other.right() || right <= other.left();
    }

    public int where(int point) {
      if (left <= point && point < right) {
        return 0;
      }
      if (left > point) {
        return point - left;
      }
      return point + 1 - right;
    }
  }

  public interface Operation<T> {
    T func(T l, T r);

    T neutral();
  }

  public static class Max implements Operation<Integer> {
    public Integer func(Integer l, Integer r) {
      if (l == null && r == null) {
        return null;
      }
      if (l == null) {
        return r;
      }
      if (r == null) {
        return l;
      }
      return (l.compareTo(r) > 0) ? l : r;
    }

    public Integer neutral() {
      return 0;
    }
  }

  public static class SegmentTree {
    private final List<Integer> array;
    private final int size;
    private final Operation<Integer> operation;

    public SegmentTree(int length, Operation<Integer> operation) {
      this.operation = operation;
      var tmp = new Integer[length << 2];
      Arrays.fill(tmp, 0);
      this.array = Arrays.asList(tmp);
      this.size = length;
    }

    private void update(int cur) {
      Integer newNode = operation.func(this.array.get(2 * cur + 1), this.array.get(2 * cur + 2));
      this.array.set(cur, newNode);
    }

    public Integer get(int left, int right) {
      return get(0, new Range(0, size), new Range(left, right + 1));
    }

    public Integer get(Range seg) {
      return get(0, new Range(0, size), seg);
    }

    public Integer get(int index) {
      return get(0, new Range(0, size), new Range(index, index + 1));
    }

    private Integer get(int cur, Range curSeg, Range absSeg) {
      if (absSeg.isOutside(curSeg) || curSeg.length() == 0) {
        return operation.neutral();
      }
      if (absSeg.isInside(curSeg)) {
        return this.array.get(cur);
      }
      Range leftRange = new Range(curSeg.left(), curSeg.middle());
      Range rightRange = new Range(curSeg.middle(), curSeg.right());
      return operation.func(
          get(2 * cur + 1, leftRange, absSeg), get(2 * cur + 2, rightRange, absSeg));
    }

    public void set(Integer value, int index) {
      set(value, 0, index, new Range(0, size));
    }

    private void set(Integer value, int cur, int index, Range seg) {
      if (seg.where(index) != 0) {
        return;
      }
      if (seg.length() == 1) {
        this.array.set(cur, value);
        return;
      }
      set(value, 2 * cur + 1, index, new Range(seg.left(), seg.middle()));
      set(value, 2 * cur + 2, index, new Range(seg.middle(), seg.right()));
      update(cur);
    }
  }

  public static class Wrapper implements Comparable<Wrapper> {
    Integer value;
    Integer index;

    public Wrapper(int value, int index) {
      this.value = value;
      this.index = -index;
    }

    @Override
    public int compareTo(Wrapper o) {
      return this.value.equals(o.value)
          ? this.index.compareTo(o.index)
          : this.value.compareTo(o.value);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Wrapper wrapper = (Wrapper) o;
      return Objects.equals(value, wrapper.value) && Objects.equals(index, wrapper.index);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value, index);
    }
  }

  public static void main(String[] args) throws IOException {
    BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer tokenizer = new StringTokenizer(buffer.readLine());
    int n = Integer.parseInt(tokenizer.nextToken());
    SegmentTree tree = new SegmentTree(n, new Max());
    Integer[] dp = new Integer[n];
    Wrapper[] st = new Wrapper[n];
    Integer result = 0;

    tokenizer = new StringTokenizer(buffer.readLine());
    for (int i = 0; i < n; ++i) {
      var tmp = Integer.parseInt(tokenizer.nextToken());
      dp[i] = tmp;
      st[i] = new Wrapper(tmp, i);
    }

    sort(st);

    for (int i = 0; i < n; ++i) {
      var ind = -st[i].index;

      dp[i] = tree.get(0, ind) + 1;
      tree.set(dp[i], ind);
      result = max(result, dp[i]);
    }

    System.out.println(result);
  }
}
