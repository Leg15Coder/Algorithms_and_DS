package structures.trees.search;

public interface AVLSearchTreeInterface<T extends Comparable<T>> extends BinarySearchTreeInterface<T>{
  T next(T value);
}
