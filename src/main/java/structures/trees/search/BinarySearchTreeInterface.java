package structures.trees.search;

public interface BinarySearchTreeInterface<T extends Comparable<T>> {
  void add(T value);

  boolean remove(T value);

  boolean delete(T value);

  boolean get(T value);

  T getMin();

  T getMax();

  boolean isEmpty();

  void clear();

  T next(T value);

  T previous(T value);

  int size();
}
