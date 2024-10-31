package structures.trees.segmentation;

public class Range {
  private final int left;
  private final int right;

  public Range(int left, int right) {
    this.left = left;
    this.right = right;
  }

  public int getLeft() {
    return left;
  }

  public int getRight() {
    return right;
  }

  public int length() {
    return right - left;
  }

  public int middle() {
    return (left + right) / 2;
  }

  public boolean isInside(Range other) {
    return left <= other.getLeft() && other.getRight() <= right;
  }

  public boolean isOutside(Range other) {
    return left > other.getRight() || right < other.getLeft();
  }

  public int where(int point) {
    if (left <= point && point < right) {
      return 0;
    }
    if (left > point) {
      return point - left;
    }
    return point - right;
  }
}
