package structures.trees.search;

public class SummarisingTree<T extends Comparable<T>> {
  protected class Node {
    Node left = null;
    Node right = null;
    T value;
    T sum;

    public Node(T value) {
      this.value = value;
    }

    public void update() {
      T result = value;
      if (left != null) {
        result += left.value;
      }
    }
  }

  Node root = null;

}
