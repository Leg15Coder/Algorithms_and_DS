package structures.arrays;

import structures.trees.search.BinarySearchTreeInterface;

public interface AutoSortedArrayInterface<T extends Comparable<T>> extends BinarySearchTreeInterface<T>, ArrayInterface<T> {
  T sum(int left, int right);

  T sum(T min, T max);

  int getIndex(T value);
}
