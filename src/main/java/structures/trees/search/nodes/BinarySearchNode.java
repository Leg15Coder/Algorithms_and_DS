package structures.trees.search.nodes;

public class BinarySearchNode<T> extends BasicNode<T> implements CounterNodeInterface<T> {
  long count = 0;

  public BinarySearchNode(T value) {
    this.value = value;
    increase();
  }

  @Override
  public void increase() {
    this.count++;
  }

  @Override
  public void decrease() {
    this.count--;
  }

  @Override
  public boolean isEmpty() {
    return this.count <= 0;
  }

  @Override
  public long getCount() {
    return this.count;
  }

  @Override
  public void setCount(long count) {
    this.count = count;
  }
}
