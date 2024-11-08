package structures.trees.search.nodes;

public interface PriorityNodeInterface<T extends Comparable<T>> extends NodeInterface<T>, Comparable<PriorityNodeInterface<T>> {
  int getPriority();
}
