package structures.arrays;

import structures.common.Pair;
import structures.trees.search.Treap;

public class AutoSortedArray<T extends Comparable<T>> extends Treap<T> implements AutoSortedArrayInterface<T> {
  @Override
  protected void updateNode(Treap<T>.Node node) {
    if (node.getLeft() == null && node.getRight() == null) {
      ((Node) node.getSubNode()).index = 0;
      ((Node) node.getSubNode()).subtreesSize = 1;
      ((Node) node.getSubNode()).sum = node.getValue();
    } else if (node.getLeft() == null) {
      ((Node) node.getSubNode()).index = 0;
      ((Node) node.getSubNode()).subtreesSize = 1 + ((Node) node.getRight().getSubNode()).getSize();
      ((Node) node.getSubNode()).sum = node.getValue();
    } else if (node.getRight() == null) {
      ((Node) node.getSubNode()).index = ((Node) node.getLeft().getSubNode()).getSize();
      ((Node) node.getSubNode()).subtreesSize = 1 + ((Node) node.getLeft().getSubNode()).getSize();
      ((Node) node.getSubNode()).sum = node.getValue();
    } else {
      ((Node) node.getSubNode()).index = ((Node) node.getLeft().getSubNode()).getSize();
      ((Node) node.getSubNode()).subtreesSize = 1 + ((Node) node.getLeft().getSubNode()).getSize() + ((Node) node.getRight().getSubNode()).getSize();
      ((Node) node.getSubNode()).sum = node.getValue();
    }
  }

  private class Node {
    int subtreesSize;
    int index;
    T sum; // todo later

    public int getSize() {
      return this.subtreesSize;
    }

    public int getIndex() {
      return this.index;
    }

    public T getSum() {
      return sum;
    }
  }

  private int checkIndex(int index) {
    if (index < -getSize() || index >= getSize()) {
      throw new  IllegalArgumentException("Выход за границы массива");
    }
    if (index < 0) {
      return getSize() + index;
    }
    return index;
  }

  @Override
  public void add(T value, int priority) {
    Node expansion = new Node();
    if (isEmpty()) {
      this.root = new Treap<T>.Node(value, priority, expansion);
      return;
    }
    Pair<Treap<T>.Node, Treap<T>.Node> tmp = splitMoreOrEq(this.root, value);
    this.root = merge(tmp.first, merge(tmp.second, new Treap<T>.Node(value, priority, expansion)));
  }

  @Override
  public T getAt(int index) {
    index = checkIndex(index);
    Treap<T>.Node cur = super.root;
    while (index >= 0) {
      if (((Node) cur.getSubNode()).getIndex() == index) {
        return cur.getValue();
      } else if (((Node) cur.getSubNode()).getIndex() < index) {
        index -= ((Node) cur.getSubNode()).getIndex() + 1;
        cur = cur.getRight();
      } else {
        cur = cur.getLeft();
      }
    }
    return null;
  }

  @Override
  public T pop() {
    if (isEmpty()) {
      throw new UnsupportedOperationException("Нельзя удалить последний элемент в пустом массиве");
    }
    T result = getAt(getSize() - 1);
    remove(getSize() - 1);
    return result;
  }

  @Override
  public void remove(int index) {
    T element = getAt(index);
    super.remove(element);
  }

  @Override
  public void insert(T value, int index) {
    throw new UnsupportedOperationException("Вставка в AutoSortedArray происходит без указания индекса, он подбирается автоматически");
  }

  @Override
  public void insert(T value) {
    add(value);
  }

  @Override
  public T sum(int left, int right) {
    left = checkIndex(left);
    right = checkIndex(right);
    if (right < left) {
      return null;
    }
    return sum(getAt(left), getAt(right));
  }

  @Override
  public T sum(T min, T max) {
    Pair<Treap<T>.Node, Treap<T>.Node> leftPair = super.splitMoreOrEq(this.root, min);
    Pair<Treap<T>.Node, Treap<T>.Node> rightPair = super.splitLessOrEq(leftPair.second, max);
    return ((Node) rightPair.first.getSubNode()).getSum();
  }

  @Override
  public int getIndex(T value) {
    if (!get(value)) {
      throw new IllegalArgumentException("Невозможно найти индекс: данного элемента нет в массиве");
    }
    return getIndex(this.root, value);
  }

  private int getIndex(Treap<T>.Node cur, T value) {
    if (cur.getValue().equals(value)) {
      return ((Node) cur.getSubNode()).getIndex();
    }
    if (cur.getValue().compareTo(value) > 0) {
      return getIndex(cur.getLeft(), value);
    }
    return ((Node) cur.getSubNode()).getIndex() + getIndex(cur.getRight(), value) + 1;
  }

  @Override
  public int getSize() {
    if (isEmpty()) {
      return 0;
    }
    return ((Node) this.root.getSubNode()).getSize();
  }
}
