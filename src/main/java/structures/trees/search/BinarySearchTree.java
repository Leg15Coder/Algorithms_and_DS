package structures.trees.search;

import static utils.Compare.max;
import static utils.Compare.min;

public class BinarySearchTree<T extends Comparable<T>> implements BinarySearchTreeInterface<T> {
  protected void updateNode(Node node) { }

  protected class Node {
    private final Object expansionPoint;

    private Node left = null;
    private Node right = null;
    private T value;
    long count = 0;

    public Node(T value, Object expansionPoint) {
      this.expansionPoint = expansionPoint;
      this.value = value;
      increase();
      updateNode(this);
    }

    public void setLeft(Node left) {
      this.left = left;
      updateNode(this);
    }

    public void setRight(Node right) {
      this.right = right;
      updateNode(this);
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

    public Object getSubNode() {
      return this.expansionPoint;
    }
  }

  protected Node root = null;
  protected int size = 0;

  @Override
  public void add(T value) {
    ++size;
    if (isEmpty()) {
      this.root = new Node(value, null);
      return;
    }
    add(this.root, value);
  }

  private void add(Node cur, T value) {
    if (cur.value.equals(value)) {
      cur.increase();
    } else if (cur.value.compareTo(value) > 0) {
      if (cur.getLeft() == null) {
        cur.setLeft(new Node(value, null));
      } else {
        add(cur.getLeft(), value);
      }
    } else {
      if (cur.getRight() == null) {
        cur.setRight(new Node(value, null));
      } else {
        add(cur.getRight(), value);
      }
    }
  }

  @Override
  public boolean remove(T value) {
    if (get(value)) {
      this.root = remove(this.root, value);
      --size;
      return true;
    }
    return false;
  }

  @Override
  public boolean delete(T value) {
    if (get(value)) {
      this.root = delete(this.root, value);
      return true;
    }
    return false;
  }

  protected Node remove(Node cur, T value) {
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
        this.size += (int) cur.count;
        cur.setRight(delete(cur.getRight(), cur.getValue()));
      }
    }
    return cur;
  }

  protected Node delete(Node cur, T value) {
    if (cur == null) {
      return null;
    }

    if (cur.getValue().compareTo(value) > 0) {
      cur.setLeft(delete(cur.getLeft(), value));
    } else if (cur.getValue().compareTo(value) < 0) {
      cur.setRight(delete(cur.getRight(), value));
    } else {
      this.size -= (int) cur.count;

      if (cur.getLeft() == null) {
        return cur.getRight();
      }
      if (cur.getRight() == null) {
        return cur.getLeft();
      }

      Node mn = getMin(cur.getRight());
      cur.value = mn.value;
      cur.count = mn.count;
      this.size += (int) cur.count;

      cur.setRight(delete(cur.getRight(), cur.getValue()));
    }
    return cur;
  }

  @Override
  public boolean get(T value) {
    if (isEmpty()) {
      return false;
    }

    Node cur = this.root;
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
    return getMin(this.root).value;
  }

  protected Node getMin(Node cur) {
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

  protected Node getMax(Node cur) {
    if (cur.getRight() == null) {
      return cur;
    }
    return getMax(cur.getRight());
  }

  @Override
  public boolean isEmpty() {
    return root == null;
  }

  @Override
  public void clear() {
    this.size = 0;
    this.root = null;
  }

  @Override
  public int getSize() {
    return size;
  }
}
