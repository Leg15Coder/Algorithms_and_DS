package structures.trees.search;

import structures.common.Pair;
import java.util.Random;

import static utils.Compare.max;
import static utils.Compare.min;

public class Treap<T extends Comparable<T>> implements BinarySearchTreeInterface<T> {
  protected class Node implements Comparable<Node> {
    T key;
    int priority;
    Node left;
    Node right;

    public Node(T key, int priority) {
      this.key = key;
      this.priority = priority;
    }

    public void setLeft(Node left) {
      this.left = left;
    }

    public void setRight(Node right) {
      this.right = right;
    }

    public Node getLeft() {
      return this.left;
    }

    public Node getRight() {
      return this.right;
    }

    public T getValue() {
      return this.key;
    }

    public int getPriority() {
      return this.priority;
    }

    @Override
    public int compareTo(Node o) {
      if (this.getPriority() > o.getPriority()) {
        return 1;
      }
      if (this.getPriority() < o.getPriority()) {
        return -1;
      }
      return Integer.compare(this.getValue().compareTo(o.getValue()), 0);
    }

    public String toString(Node parent) {
      String result = "";
      String cur = "";
      if (parent == null) {
        cur += "0 ";
      } else {
        cur += parent.getValue() + " ";
      }
      if (this.getLeft() != null) {
        result += this.getLeft().toString(this);
        cur += this.getLeft().getValue() + " ";
      } else {
        cur += "0 ";
      }
      if (this.getRight() != null) {
        result += this.getRight().toString(this);
        cur += this.getRight().getValue() + "";
      } else {
        cur += "0";
      }
      result += "\n" + cur;
      return result;
    }
  }

  protected Random random = new Random();
  protected Node root = null;

  protected Node merge(Node left, Node right) {
    if (left == null) {
      return right;
    }
    if (right == null) {
      return left;
    }
    if (left.compareTo(right) > 0) {
      Node tmp = left;
      left = right;
      right = tmp;
    }
    if (left.getValue().compareTo(right.getValue()) > 0) {
      left.setLeft(merge(left.getLeft(), right));
    } else {
      left.setRight(merge(left.getRight(), right));
    }
    return left;
  }

  protected Pair<Node, Node> splitLessOrEq(Node cur, T value) {
    if (cur == null) {
      return new Pair<>(null, null);
    }
    if (cur.getValue().compareTo(value) > 0) {
      Pair<Node, Node> tmp = splitLessOrEq(cur.getLeft(), value);
      cur.setLeft(tmp.second);
      return new Pair<>(tmp.first, cur);
    } else {
      Pair<Node, Node> tmp = splitLessOrEq(cur.getRight(), value);
      cur.setRight(tmp.first);
      return new Pair<>(cur, tmp.second);
    }
  }

  protected Pair<Node, Node> splitMoreOrEq(Node cur, T value) {
    if (cur == null) {
      return new Pair<>(null, null);
    }
    if (cur.getValue().compareTo(value) < 0) {
      Pair<Node, Node> tmp = splitMoreOrEq(cur.getRight(), value);
      cur.setRight(tmp.first);
      return new Pair<>(cur, tmp.second);
    } else {
      Pair<Node, Node> tmp = splitMoreOrEq(cur.getLeft(), value);
      cur.setLeft(tmp.second);
      return new Pair<>(tmp.first, cur);
    }
  }

  @Override
  public void add(T value) {
    int priority = random.nextInt();
    add(value, priority);
  }

  public void add(T value, int priority) {
    if (isEmpty()) {
      this.root = new Node(value, priority);
      return;
    }
    Pair<Node, Node> tmp = splitMoreOrEq(this.root, value);
    this.root = merge(tmp.first, merge(tmp.second, new Node(value, priority)));
  }

  @Override
  public boolean remove(T value) {
    if (get(value)) {
      Pair<Node, Node> less = splitMoreOrEq(this.root, value);
      Pair<Node, Node> more = splitLessOrEq(less.second, value);
      this.root = merge(less.first, merge(merge(more.first.getLeft(), more.first.getRight()), more.second));
      return true;
    }
    return false;
  }

  @Override
  public boolean delete(T value) {
    if (get(value)) {
      Pair<Node, Node> less = splitMoreOrEq(this.root, value);
      Pair<Node, Node> more = splitLessOrEq(less.second, value);
      this.root = merge(less.first, more.second);
      return true;
    }
    return false;
  }

  @Override
  public boolean get(T value) {
    if (isEmpty()) {
      return false;
    }
    Node cur = (Node) this.root;
    while (cur != null) {
      if (cur.getValue().equals(value)) {
        return true;
      } else if (cur.getValue().compareTo(value) > 0) {
        cur = cur.getLeft();
      } else {
        cur = cur.getRight();
      }
    }
    return false;
  }

  @Override
  public T getMin() {
    if (isEmpty()) {
      return null;
    }
    Node cur = this.root;
    while (cur.getLeft() != null) {
      cur = cur.getLeft();
    }
    return cur.getValue();
  }

  @Override
  public T getMax() {
    if (isEmpty()) {
      return null;
    }
    Node cur = this.root;
    while (cur.getRight() != null) {
      cur = cur.getRight();
    }
    return cur.getValue();
  }

  @Override
  public T next(T value) {
    if (isEmpty()) {
      return null;
    }
    Node cur = this.root;
    T result = null;
    while (cur != null) {
      if (cur.getValue().equals(value)) {
        return value;
      } else if (cur.getValue().compareTo(value) < 0) {
        cur = cur.getRight();
      } else {
        result = min(result, cur.getValue());
        cur = cur.getLeft();
      }
    }
    return result;
  }

  @Override
  public T previous(T value) {
    if (isEmpty()) {
      return null;
    }
    Node cur = this.root;
    T result = null;
    while (cur != null) {
      if (cur.getValue().equals(value)) {
        return value;
      } else if (cur.getValue().compareTo(value) > 0) {
        cur = cur.getLeft();
      } else {
        result = max(result, cur.getValue());
        cur = cur.getRight();
      }
    }
    return result;
  }

  @Override
  public boolean isEmpty() {
    return root == null;
  }

  @Override
  public void clear() {
    this.root = null;
  }

  @Override
  public int getSize() {
    return 0; // todo later
  }

  @Override
  public String toString() {
    if (isEmpty()) {
      return "null";
    }
    return this.root.toString(null);
  }
}
