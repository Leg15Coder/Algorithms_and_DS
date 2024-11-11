package structures.trees.search;

import structures.common.Pair;
import structures.trees.search.nodes.PriorityNodeInterface;
import structures.trees.search.nodes.TreapNode;

import java.util.Random;

import static utils.Compare.max;
import static utils.Compare.min;

public class Treap<T extends Comparable<T>> implements BinarySearchTreeInterface<T> {
  protected Random random = new Random();
  protected Node root = null;

  protected PriorityNodeInterface<T> merge(PriorityNodeInterface<T> left, PriorityNodeInterface<T> right) {
    if (left == null || left.getValue() == null) {
      return right;
    }
    if (right == null || right.getValue() == null) {
      return left;
    }
    if (left.compareTo(right) > 0) {
      TreapNode<T> tmp = (TreapNode<T>) left;
      left = right;
      right = tmp;
    }
    if (left.getValue().compareTo(right.getValue()) > 0) {
      left.setLeft(merge((PriorityNodeInterface<T>) left.getLeft(), right));
    } else {
      left.setRight(merge((PriorityNodeInterface<T>) left.getRight(), right));
    }
    return left;
  }

  protected Pair<PriorityNodeInterface<T>, PriorityNodeInterface<T>> splitLessOrEq(PriorityNodeInterface<T> cur, T value) {
    if (cur == null || cur.getValue() == null) {
      return new Pair<>(null, null);
    }
    if (cur.getValue().compareTo(value) > 0) {
      Pair<PriorityNodeInterface<T>, PriorityNodeInterface<T>> tmp = splitLessOrEq((PriorityNodeInterface<T>) cur.getLeft(), value);
      cur.setLeft(tmp.second);
      return new Pair<>(tmp.first, cur);
    } else {
      Pair<PriorityNodeInterface<T>, PriorityNodeInterface<T>> tmp = splitLessOrEq((PriorityNodeInterface<T>) cur.getRight(), value);
      cur.setRight(tmp.first);
      return new Pair<>(cur, tmp.second);
    }
  }

  protected Pair<PriorityNodeInterface<T>, PriorityNodeInterface<T>> splitMoreOrEq(PriorityNodeInterface<T> cur, T value) {
    if (cur == null || cur.getValue() == null) {
      return new Pair<>(null, null);
    }
    if (cur.getValue().compareTo(value) < 0) {
      Pair<PriorityNodeInterface<T>, PriorityNodeInterface<T>> tmp = splitMoreOrEq((PriorityNodeInterface<T>) cur.getRight(), value);
      cur.setRight(tmp.first);
      return new Pair<>(cur, tmp.second);
    } else {
      Pair<PriorityNodeInterface<T>, PriorityNodeInterface<T>> tmp = splitMoreOrEq((PriorityNodeInterface<T>) cur.getLeft(), value);
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
      this.root = new TreapNode<>(value, priority);
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
    TreapNode<T> cur = (TreapNode<T>) this.root;
    while (cur != null && cur.getValue() != null) {
      if (cur.getValue().equals(value)) {
        return true;
      } else if (cur.getValue().compareTo(value) > 0) {
        cur = (TreapNode<T>) cur.getLeft();
      } else {
        cur = (TreapNode<T>) cur.getRight();
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
      cur = (TreapNode<T>) cur.getLeft();
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
      cur = (TreapNode<T>) cur.getRight();
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
    while (cur != null && cur.getValue() != null) {
      if (cur.getValue().equals(value)) {
        return value;
      } else if (cur.getValue().compareTo(value) < 0) {
        cur = (TreapNode<T>) cur.getRight();
      } else {
        result = min(result, cur.getValue());
        cur = (TreapNode<T>) cur.getLeft();
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
    while (cur != null && cur.getValue() != null) {
      if (cur.getValue().equals(value)) {
        return value;
      } else if (cur.getValue().compareTo(value) > 0) {
        cur = (TreapNode<T>) cur.getLeft();
      } else {
        result = max(result, cur.getValue());
        cur = (TreapNode<T>) cur.getRight();
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
