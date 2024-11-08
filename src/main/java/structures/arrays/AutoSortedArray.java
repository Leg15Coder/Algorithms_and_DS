package structures.arrays;

import structures.common.Pair;
import structures.trees.search.Treap;
import structures.trees.search.nodes.ArrayTreapNode;
import structures.trees.search.nodes.PriorityNodeInterface;

public class AutoSortedArray<T extends Comparable<T>> extends Treap<T> implements AutoSortedArrayInterface<T> {
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
    ArrayTreapNode<T> cur = (ArrayTreapNode<T>) this.root;
    while (index > 0) {
      if (cur.getIndex() == index) {
        return cur.getValue();
      } else if (cur.getIndex() < index) {
        index -= cur.getIndex();
        cur = (ArrayTreapNode<T>) cur.getRight();
      } else {
        cur = (ArrayTreapNode<T>) cur.getLeft();
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
    Pair<PriorityNodeInterface<T>, PriorityNodeInterface<T>> leftPair = super.splitMoreOrEq(this.root, min);
    Pair<PriorityNodeInterface<T>, PriorityNodeInterface<T>> rightPair = super.splitLessOrEq(leftPair.second, max);
    return ((ArrayTreapNode<T>) rightPair.first).getSum();
  }

  @Override
  public int getIndex(T value) {
    if (!get(value)) {
      throw new IllegalArgumentException("Данного элемента нет в массиве");
    }
    return getIndex((ArrayTreapNode<T>) this.root, value);
  }

  private int getIndex(ArrayTreapNode<T> cur, T value) {
    if (cur.getValue().equals(value)) {
      return cur.getIndex();
    }
    if (cur.getValue().compareTo(value) > 0) {
      return getIndex((ArrayTreapNode<T>) cur.getLeft(), value);
    }
    return cur.getIndex() + getIndex((ArrayTreapNode<T>) cur.getRight(), value);
  }

  @Override
  public int getSize() {
    if (isEmpty()) {
      return 0;
    }
    return ((ArrayTreapNode<T>) this.root).getSize();
  }
}
