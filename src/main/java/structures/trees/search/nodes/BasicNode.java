package structures.trees.search.nodes;

abstract public class BasicNode<T> implements NodeInterface<T> {
  NodeInterface<T> left = null;
  NodeInterface<T> right = null;
  T value;

  @Override
  public void setLeft(NodeInterface<T> left) {
    this.left = left;
  }

  @Override
  public void setRight(NodeInterface<T> right) {
    this.right = right;
  }

  @Override
  public NodeInterface<T> getLeft() {
    return this.left;
  }

  @Override
  public NodeInterface<T> getRight() {
    return this.right;
  }

  public T getValue() {
    return this.value;
  }

  @Override
  public void setValue(T value) {
    this.value = value;
  }
}
