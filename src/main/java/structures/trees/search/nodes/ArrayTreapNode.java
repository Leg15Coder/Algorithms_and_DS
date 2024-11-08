package structures.trees.search.nodes;

public class ArrayTreapNode<T extends Comparable<T>> extends TreapNode<T> implements SummiringNodeInterface<T>, PriorityNodeInterface<T> {
  T sum; // todo later
  int index;
  int subtreesSize;

  public ArrayTreapNode(T key, int priority) {
    super(key, priority);
    update();
  }

  private void update() {
    if (getLeft() == null && getRight() == null) {
      this.index = 0;
      this.subtreesSize = 1;
      this.sum = getValue();
    } else if (getLeft() == null) {
      this.index = 0;
      this.subtreesSize = 1 + ((ArrayTreapNode<T>) getRight()).getSize();
      this.sum = getValue();
    } else if (getRight() == null) {
      this.index = ((ArrayTreapNode<T>) getLeft()).getIndex();
      this.subtreesSize = 1 + ((ArrayTreapNode<T>) getLeft()).getSize();
      this.sum = getValue();
    } else {
      this.index = ((ArrayTreapNode<T>) getLeft()).getIndex();
      this.subtreesSize = 1 + ((ArrayTreapNode<T>) getLeft()).getSize() + ((ArrayTreapNode<T>) getRight()).getSize();
      this.sum = getValue();
    }
  }

  public int getSize() {
    return this.subtreesSize;
  }

  public int getIndex() {
    return this.index;
  }

  public void setLeft(NodeInterface<T> left) {
    super.setLeft(left);
    update();
  }

  public void setRight(NodeInterface<T> right) {
    super.setRight(right);
    update();
  }

  public T getValue() {
    update();
    return super.getValue();
  }

  public T getSum() {
    update();
    return sum;
  }
}
