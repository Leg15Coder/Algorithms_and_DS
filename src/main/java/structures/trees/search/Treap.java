package structures.trees.search;

import structures.common.Pair;
import java.util.Random;

import static utils.Compare.max;
import static utils.Compare.min;

public class Treap<T extends Comparable<T>> extends BinarySearchTree<T> {
  protected class Node {
    int priority;

    public Node(int priority) {
      this.priority = priority;
    }

    public int getPriority() {
      return this.priority;
    }

    public int compareToWith(Node o, T thisValue, T otherValue) {
      if (this.getPriority() > o.getPriority()) {
        return 1;
      }
      if (this.getPriority() < o.getPriority()) {
        return -1;
      }
      return Integer.compare(thisValue.compareTo(otherValue), 0);
    }
  }

  protected Random random = new Random();

  protected BinarySearchTree<T>.Node merge(BinarySearchTree<T>.Node left, BinarySearchTree<T>.Node right) {
    if (left == null) {
      return right;
    }
    if (right == null) {
      return left;
    }
    if (((Node) left.getSubNode()).compareToWith(((Node) right.getSubNode()), left.getValue(), right.getValue()) > 0) {
      BinarySearchTree<T>.Node tmp = left;
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

  protected Pair<BinarySearchTree<T>.Node, BinarySearchTree<T>.Node> splitLessOrEq(BinarySearchTree<T>.Node cur, T value) {
    if (cur == null) {
      return new Pair<>(null, null);
    }
    if (cur.getValue().compareTo(value) > 0) {
      Pair<BinarySearchTree<T>.Node, BinarySearchTree<T>.Node> tmp = splitLessOrEq(cur.getLeft(), value);
      cur.setLeft(tmp.second);
      return new Pair<>(tmp.first, cur);
    } else {
      Pair<BinarySearchTree<T>.Node, BinarySearchTree<T>.Node> tmp = splitLessOrEq(cur.getRight(), value);
      cur.setRight(tmp.first);
      return new Pair<>(cur, tmp.second);
    }
  }

  protected Pair<BinarySearchTree<T>.Node, BinarySearchTree<T>.Node> splitMoreOrEq(BinarySearchTree<T>.Node cur, T value) {
    if (cur == null) {
      return new Pair<>(null, null);
    }
    if (cur.getValue().compareTo(value) < 0) {
      Pair<BinarySearchTree<T>.Node, BinarySearchTree<T>.Node> tmp = splitMoreOrEq(cur.getRight(), value);
      cur.setRight(tmp.first);
      return new Pair<>(cur, tmp.second);
    } else {
      Pair<BinarySearchTree<T>.Node, BinarySearchTree<T>.Node> tmp = splitMoreOrEq(cur.getLeft(), value);
      cur.setLeft(tmp.second);
      return new Pair<>(tmp.first, cur);
    }
  }

  @Override
  public void add(T value) {
    ++size;
    int priority = random.nextInt();
    add(value, priority);
  }

  public void add(T value, int priority) {
    if (isEmpty()) {
      this.root = new BinarySearchTree<T>.Node(value, new Node(priority));
      return;
    }
    Pair<BinarySearchTree<T>.Node, BinarySearchTree<T>.Node> tmp = splitMoreOrEq(this.root, value);
    this.root = merge(tmp.first, merge(tmp.second, new BinarySearchTree<T>.Node(value, new Node(priority))));
  }

  @Override
  public boolean remove(T value) {
    if (get(value)) {
      Pair<BinarySearchTree<T>.Node, BinarySearchTree<T>.Node> less = splitMoreOrEq(this.root, value);
      Pair<BinarySearchTree<T>.Node, BinarySearchTree<T>.Node> more = splitLessOrEq(less.second, value);
      this.root = merge(less.first, merge(merge(more.first.getLeft(), more.first.getRight()), more.second));
      --size;
      return true;
    }
    return false;
  }

  @Override
  public boolean delete(T value) {
    if (get(value)) {
      Pair<BinarySearchTree<T>.Node, BinarySearchTree<T>.Node> less = splitMoreOrEq(this.root, value);
      Pair<BinarySearchTree<T>.Node, BinarySearchTree<T>.Node> more = splitLessOrEq(less.second, value);
      this.root = merge(less.first, more.second);
      size -= count(less.second) + count(more.first);
      return true;
    }
    return false;
  }

  @Override
  public boolean get(T value) {
    if (isEmpty()) {
      return false;
    }

    BinarySearchTree<T>.Node cur = this.root;
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
    BinarySearchTree<T>.Node cur = this.root;
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
    BinarySearchTree<T>.Node cur = this.root;
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
    BinarySearchTree<T>.Node cur = this.root;
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
    BinarySearchTree<T>.Node cur = this.root;
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

  private int count(BinarySearchTree<T>.Node cur) {
    if (cur == null) {
      return 0;
    }

    return (int) (count(cur.getLeft()) + cur.count + count(cur.getRight()));
  }
}
