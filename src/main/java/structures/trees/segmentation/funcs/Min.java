package structures.trees.segmentation.funcs;

import structures.trees.segmentation.Operation;

public class Min<T extends Comparable<T>> implements Operation<T> {
  @Override
  public T func(T left, T right) {
    if (right == null) {
      return left;
    }
    if (left == null) {
      return right;
    }
    return (left.compareTo(right) > 0) ? right : left;
  }

  @Override
  public T neutral() {
    return null;
  }
}
