package structures.arrays;

import structures.common.Pair;
import structures.trees.search.Treap;

public class AutoSortedArray<T extends Comparable<T>> extends Treap<T> implements AutoSortedArrayInterface<T> {
  private class Node extends Treap<T>.Node {
    T sum; // todo later
    int index;
    int subtreesSize;

    public Node(T key, int priority) {
      super(key, priority);
      update();
    }

    private void update() {
      if (getLeft() == null && getRight() == null) {
        this.index = 0;
        this.subtreesSize = 1;
        this.sum = getValue();
      } else if (getLeft() == null) {
        this.index = 0;
        this.subtreesSize = 1 + ((Node) getRight()).getSize();
        this.sum = getValue();
      } else if (getRight() == null) {
        this.index = ((Node) getLeft()).getIndex();
        this.subtreesSize = 1 + ((Node) getLeft()).getSize();
        this.sum = getValue();
      } else {
        this.index = ((Node) getLeft()).getIndex();
        this.subtreesSize = 1 + ((Node) getLeft()).getSize() + ((Node) getRight()).getSize();
        this.sum = getValue();
      }
    }

    public int getSize() {
      return this.subtreesSize;
    }

    public int getIndex() {
      return this.index;
    }

    public void setLeft(Node left) {
      super.setLeft(left);
      update();
    }

    public void setRight(Node right) {
      super.setRight(right);
      update();
    }

    public T getValue() {
      update();
      return super.getValue();
    }

    public T getSum() {
      update();
      return sum;
    }
  }

  private int checkIndex(int index) {
    if (index < -getSize() || index >= getSize()) {
      throw new  IllegalArgumentException("Выход за границы массива");
    }
    if (index < 0) {
      return getSize() - index;
    }
    return index;
  }

  @Override
  public T getAt(int index) {
    if (index < -getSize() || index >= getSize()) {
      throw new IllegalArgumentException("Выход за границы массива");
    }
    Node cur = (Node) this.root;
    while (index > 0) {
      if (cur.getIndex() == index) {
        return cur.getValue();
      } else if (cur.getIndex() < index) {
        index -= cur.getIndex();
        cur = (Node) cur.getRight();
      } else {
        cur = (Node) cur.getLeft();
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
    super.add(value);
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
    Pair<Treap<T>.Node, Treap<T>.Node> leftPair = super.splitMoreOrEq((Treap<T>.Node) this.root, min);
    Pair<Treap<T>.Node, Treap<T>.Node> rightPair = super.splitLessOrEq(leftPair.second, max);
    return ((Node) rightPair.first).getSum();
  }

  @Override
  public int getIndex(T value) {
    if (!get(value)) {
      throw new IllegalArgumentException("Данного элемента нет в массиве");
    }
    return getIndex((Node) this.root, value);
  }

  private int getIndex(Node cur, T value) {
    if (cur.getValue().equals(value)) {
      return cur.getIndex();
    }
    if (cur.getValue().compareTo(value) > 0) {
      return getIndex((Node) cur.getLeft(), value);
    }
    return cur.getIndex() + getIndex((Node) cur.getRight(), value);
  }

  @Override
  public int getSize() {
    if (isEmpty()) {
      return 0;
    }
    return ((Node) this.root).getSize();
  }
}
