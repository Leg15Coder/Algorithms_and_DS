package structures.trees.search.nodes;

public interface NodeInterface<T> {
  void setLeft(NodeInterface<T> left);

  void setRight(NodeInterface<T> right);

  NodeInterface<T> getLeft();

  NodeInterface<T> getRight();

  T getValue();

  void setValue(T value);
}
