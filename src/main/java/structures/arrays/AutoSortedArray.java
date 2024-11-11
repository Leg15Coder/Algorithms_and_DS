package structures.arrays;

import structures.common.Pair;
import structures.trees.search.Treap;
import structures.trees.search.nodes.ArrayTreapNode;
import structures.trees.search.nodes.PriorityNodeInterface;

public class AutoSortedArray<T extends Comparable<T>> extends Treap<T> implements AutoSortedArrayInterface<T> {
  private class Node implements Comparable<Node> {
    Treap<T>.Node core;
    T sum; // todo later
    int index;
    int subtreesSize;
    Node left;
    Node right;

    public Node(T key, int priority) {
      this.core = new Treap<T>.Node(key, priority);
      update();
    }

    public Node(Treap<T>.Node core, Node left, Node right) {
      this.core = core;
      this.left = left;
      this.right = right;
      update();
    }

    private void update() {
      if (getLeft() == null && getRight() == null) {
        this.index = 0;
        this.subtreesSize = 1;
        this.sum = getValue();
      } else if (getLeft() == null) {
        this.index = 0;
        this.subtreesSize = 1 + (getRight()).getSize();
        this.sum = getValue();
      } else if (getRight() == null) {
        this.index = (getLeft()).getIndex();
        this.subtreesSize = 1 + getLeft().getSize();
        this.sum = getValue();
      } else {
        this.index = (getLeft()).getIndex();
        this.subtreesSize = 1 + getLeft().getSize() + getRight().getSize();
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
      core.setLeft(left.core);
      this.left = left;
      update();
    }

    public void setRight(Node right) {
      core.setRight(right.core);
      this.right = left;
      update();
    }

    public T getValue() {
      update();
      return core.getValue();
    }
    public Node getLeft() {
      return this.left;
    }

    public Node getRight() {
      return this.right;
    }

    public int getPriority() {
      return core.getPriority();
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

    public T getSum() {
      update();
      return sum;
    }
  }

  protected Node root;

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
    Node cur = this.root;
    while (index > 0) {
      if (cur.getIndex() == index) {
        return cur.getValue();
      } else if (cur.getIndex() < index) {
        index -= cur.getIndex();
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
    // Pair<Treap<T>.Node, Treap<T>.Node> leftPair = super.splitMoreOrEq((Treap<T>.Node) this.root, min);
    // Pair<Treap<T>.Node, Treap<T>.Node> rightPaSir = super.splitLessOrEq(leftPair.second, max);
    // return rightPair.first.getSum();
    return null;
  }

  @Override
  public int getIndex(T value) {
    if (!get(value)) {
      throw new IllegalArgumentException("Данного элемента нет в массиве");
    }
    return getIndex(this.root, value);
  }

  private int getIndex(ArrayTreapNode<T> cur, T value) {
    if (cur.getValue().equals(value)) {
      return cur.getIndex();
    }
    if (cur.getValue().compareTo(value) > 0) {
      return getIndex(cur.getLeft(), value);
    }
    return cur.getIndex() + getIndex(cur.getRight(), value);
  }

  @Override
  public int getSize() {
    if (isEmpty()) {
      return 0;
    }
    return this.root.getSize();
  }
}
