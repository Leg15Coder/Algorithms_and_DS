package structures.trees.search.nodes;

public interface CounterNodeInterface<T> extends NodeInterface<T> {
  void increase();

  void decrease();

  boolean isEmpty();

  long getCount();

  void setCount(long count);
}
