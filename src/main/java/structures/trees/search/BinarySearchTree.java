package structures.trees.search;

import static utils.Compare.max;
import static utils.Compare.min;

public class BinarySearchTree<T extends Comparable<T>> implements BinarySearchTreeInterface<T> {
  protected class Node {
    Node left = null;
    Node right = null;
    T value;
    long count = 0;

    public Node(T value) {
      this.value = value;
      increase();
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
      return this.value;
    }

    public void increase() {
      this.count++;
    }

    public void decrease() {
      this.count--;
    }

    public boolean empty() {
      return this.count <= 0;
    }
  }

  Node root = null;
  int size = 0;

  @Override
  public void add(T value) {
    size++;
    if (isEmpty()) {
      this.root = new Node(value);
      return;
    }
    add(this.root, value);
  }

  private void add(Node cur, T value) {
    if (cur.value.equals(value)) {
      cur.increase();
    } else if (cur.value.compareTo(value) > 0) {
      if (cur.getLeft() == null) {
        cur.setLeft(new Node(value));
      } else {
        add(cur.getLeft(), value);
      }
    } else {
      if (cur.getRight() == null) {
        cur.setRight(new Node(value));
      } else {
        add(cur.getRight(), value);
      }
    }
  }

  @Override
  public boolean remove(T value) {
    if (get(this.root, value)) {
      this.root = remove(this.root, value);
      size--;
      return true;
    }
    return false;
  }

  @Override
  public boolean delete(T value) {
    return false; // todo later
  }

  private Node remove(Node cur, T value) {
    if (cur == null) {
      return null;
    }
    if (cur.getValue().compareTo(value) > 0) {
      cur.setLeft(remove(cur.getLeft(), value));
    } else if (cur.getValue().compareTo(value) < 0) {
      cur.setRight(remove(cur.getRight(), value));
    } else {
      cur.decrease();
      if (cur.empty()) {
        if (cur.getLeft() == null) {
          return cur.getRight();
        }
        if (cur.getRight() == null) {
          return cur.getLeft();
        }
        Node mn = getMin(cur.getRight());
        cur.value = mn.value;
        cur.count = mn.count;
        cur.setRight(delete(cur.getRight(), cur.getValue()));
      }
    }
    return cur;
  }

  private Node delete(Node cur, T value) {
    if (cur == null) {
      return null;
    }
    if (cur.getValue().compareTo(value) > 0) {
      cur.setLeft(remove(cur.getLeft(), value));
    } else if (cur.getValue().compareTo(value) < 0) {
      cur.setRight(remove(cur.getRight(), value));
    } else {
      if (cur.getLeft() == null) {
        return cur.getRight();
      }
      if (cur.getRight() == null) {
        return cur.getLeft();
      }
      Node mn = getMin(cur.getRight());
      cur.value = mn.value;
      cur.count = mn.count;
      cur.setRight(delete(cur.getRight(), cur.getValue()));
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
    return getMin(this.root).value;
  }

  private Node getMin(Node cur) {
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
    return getMax(this.root).value;
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

  private Node getMax(Node cur) {
    if (cur.getRight() == null) {
      return cur;
    }
    return getMax(cur.getRight());
  }

  private boolean get(Node cur, T value) {
    if (cur.value.equals(value)) {
      return true;
    } else if (cur.value.compareTo(value) > 0 && cur.getLeft() != null) {
      return get(cur.getLeft(), value);
    } else if (cur.value.compareTo(value) < 0 && cur.getRight() != null) {
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
