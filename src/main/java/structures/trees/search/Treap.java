package structures.trees.search;

import structures.common.Pair;
import java.util.Random;

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

  private Pair<BinarySearchTree<T>.Node, Pair<BinarySearchTree<T>.Node, BinarySearchTree<T>.Node>> extract(T value) {
    Pair<BinarySearchTree<T>.Node, BinarySearchTree<T>.Node> less = splitMoreOrEq(this.root, value);
    Pair<BinarySearchTree<T>.Node, BinarySearchTree<T>.Node> more = splitLessOrEq(less.second, value);

    return new Pair<>(
        more.first,
        new Pair<>(less.first, more.second)
    );
  }

  @Override
  public void add(T value) {
    int priority = random.nextInt();
    add(value, priority);
  }

  public void add(T value, int priority) {
    if (isEmpty()) {
      ++size;
      this.root = new BinarySearchTree<T>.Node(value, new Node(priority));
      return;
    }

    if (get(value)) {
      super.add(value);
      return;
    }

    ++size;
    Pair<BinarySearchTree<T>.Node, BinarySearchTree<T>.Node> tmp = splitMoreOrEq(this.root, value);
    this.root = merge(tmp.first, merge(tmp.second, new BinarySearchTree<T>.Node(value, new Node(priority))));
  }

  @Override
  public boolean remove(T value) {
    var pair = extract(value);

    if (pair.first != null) {
      pair.first.decrease();

      if (!pair.first.empty()) {
        this.root = merge(pair.second.first, merge(pair.first, pair.second.second));
      } else {
        this.root = merge(pair.second.first, pair.second.second);
      }

      --size;
      return true;
    } else {
      this.root = merge(pair.second.first, pair.second.second);
    }

    return false;
  }

  @Override
  public boolean delete(T value) {
    var pair = extract(value);
    this.root = merge(pair.second.first, pair.second.second);

    if (pair.first != null) {
      this.size -= (int) pair.first.count;
      return true;
    }

    return false;
  }
}
