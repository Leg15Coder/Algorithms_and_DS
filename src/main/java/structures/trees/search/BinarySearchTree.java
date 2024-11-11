package structures.trees.search;

import structures.trees.search.nodes.BinarySearchNode;
import structures.trees.search.nodes.CounterNodeInterface;
import structures.trees.search.nodes.NodeInterface;

import static utils.Compare.max;
import static utils.Compare.min;

public class BinarySearchTree<T extends Comparable<T>> implements BinarySearchTreeInterface<T> {
  NodeInterface<T> root = null;
  int size = 0;

  @Override
  public void add(T value) {
    size++;
    if (isEmpty()) {
      this.root = new BinarySearchNode<>(value);
      return;
    }
    add((CounterNodeInterface<T>) this.root, value);
  }

  private void add(CounterNodeInterface<T> cur, T value) {
    if (cur.getValue().equals(value)) {
      cur.increase();
    } else if (cur.getValue().compareTo(value) > 0) {
      if (cur.getLeft() == null) {
        cur.setLeft(new BinarySearchNode<>(value));
      } else {
        add((CounterNodeInterface<T>) cur.getLeft(), value);
      }
    } else {
      if (cur.getRight() == null) {
        cur.setRight(new BinarySearchNode<>(value));
      } else {
        add((CounterNodeInterface<T>) cur.getRight(), value);
      }
    }
  }

  @Override
  public boolean remove(T value) {
    if (get(this.root, value)) {
      this.root = remove((CounterNodeInterface<T>) this.root, value);
      size--;
      return true;
    }
    return false;
  }

  @Override
  public boolean delete(T value) {
    return false; // todo later
  }

  private BinarySearchNode<T> remove(CounterNodeInterface<T> cur, T value) {
    if (cur == null) {
      return null;
    }
    if (cur.getValue().compareTo(value) > 0) {
      cur.setLeft(remove((CounterNodeInterface<T>) cur.getLeft(), value));
    } else if (cur.getValue().compareTo(value) < 0) {
      cur.setRight(remove((CounterNodeInterface<T>) cur.getRight(), value));
    } else {
      cur.decrease();
      if (cur.isEmpty()) {
        if (cur.getLeft() == null) {
          return (BinarySearchNode<T>) cur.getRight();
        }
        if (cur.getRight() == null) {
          return (BinarySearchNode<T>) cur.getLeft();
        }
        BinarySearchNode<T> mn = (BinarySearchNode<T>) getMin(cur.getRight());
        cur.setValue(mn.getValue());
        cur.setCount(mn.getCount());
        cur.setRight(delete((CounterNodeInterface<T>) cur.getRight(), cur.getValue()));
      }
    }
    return (BinarySearchNode<T>) cur;
  }

  private CounterNodeInterface<T> delete(CounterNodeInterface<T> cur, T value) {
    if (cur == null) {
      return null;
    }
    if (cur.getValue().compareTo(value) > 0) {
      cur.setLeft(remove((CounterNodeInterface<T>) cur.getLeft(), value));
    } else if (cur.getValue().compareTo(value) < 0) {
      cur.setRight(remove((CounterNodeInterface<T>) cur.getRight(), value));
    } else {
      if (cur.getLeft() == null) {
        return (CounterNodeInterface<T>) cur.getRight();
      }
      if (cur.getRight() == null) {
        return (CounterNodeInterface<T>) cur.getLeft();
      }
      BinarySearchNode<T> mn = (BinarySearchNode<T>) getMin(cur.getRight());
      cur.setValue(mn.getValue());
      cur.setCount(mn.getCount());
      cur.setRight(delete((CounterNodeInterface<T>) cur.getRight(), cur.getValue()));
    }
    return cur;
  }

  @Override
  public boolean get(T value) {
    if (!isEmpty()) {
      return get(this.root, value);
    }
    return false;
  }

  @Override
  public T getMin() {
    if (isEmpty()) {
      return null;
    }
    return getMin(this.root).getValue();
  }

  private NodeInterface<T> getMin(NodeInterface<T> cur) {
    if (cur.getLeft() == null) {
      return cur;
    }
    return getMin(cur.getLeft());
  }

  @Override
  public T getMax() {
    if (isEmpty()) {
      return null;
    }
    return getMax(this.root).getValue();
  }

  @Override
  public T next(T value) {
    if (isEmpty()) {
      return null;
    }
    NodeInterface<T> cur = this.root;
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
    NodeInterface<T> cur = this.root;
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

  private NodeInterface<T> getMax(NodeInterface<T> cur) {
    if (cur.getRight() == null) {
      return cur;
    }
    return getMax(cur.getRight());
  }

  private boolean get(NodeInterface<T> cur, T value) {
    if (cur.getValue().equals(value)) {
      return true;
    } else if (cur.getValue().compareTo(value) > 0 && cur.getLeft() != null) {
      return get(cur.getLeft(), value);
    } else if (cur.getValue().compareTo(value) < 0 && cur.getRight() != null) {
      return get(cur.getRight(), value);
    }
    return false;
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
    return size;
  }
}
