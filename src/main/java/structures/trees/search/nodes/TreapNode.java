package structures.trees.search.nodes;

public class TreapNode<T extends Comparable<T>> extends BasicNode<T> implements PriorityNodeInterface<T> {
  T key;
  int priority;
  NodeInterface<T> left;
  NodeInterface<T> right;

  public TreapNode(T key, int priority) {
    this.key = key;
    this.priority = priority;
  }

  public int getPriority() {
    return this.priority;
  }

  @Override
  public int compareTo(PriorityNodeInterface<T> o) {
    if (this.getPriority() > o.getPriority()) {
      return 1;
    }
    if (this.getPriority() < o.getPriority()) {
      return -1;
    }
    return Integer.compare(this.getValue().compareTo(o.getValue()), 0);
  }
}
