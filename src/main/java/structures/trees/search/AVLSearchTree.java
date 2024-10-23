package structures.trees.search;

import static utils.Compare.max;

public class AVLSearchTree<T extends Comparable<T>> implements BinarySearchTreeInterface<T> {
  protected class Node {
    Node left = null;
    Node right = null;
    T value;
    long count = 0;
    int diff = 0;
    int height = 1;
    int order = 0;

    public Node(T value) {
      this.value = value;
      increase();
      calculateDiff();
    }

    public int diff() {
      return diff;
    }

    private void calculateDiff() {
      if (left == null && right == null) {
        this.diff = 0;
        this.height = 1;
        this.order = (int) count;
      } else if (left == null) {
        this.diff = -right.height;
        this.height = right.height + 1;
        this.order = (int) count;
      } else if (right == null) {
        this.diff = left.height;
        this.height = left.height + 1;
        this.order = left.order + (int) count;
      } else {
        this.diff = left.height - right.height;
        this.height = max(left.height, right.height) + 1;
        this.order = left.order + (int) count;
      }
    }

    public void setLeft(Node left) {
      this.left = left;
      calculateDiff();
    }

    public void setRight(Node right) {
      this.right = right;
      calculateDiff();
    }

    public Node getLeft() {
      calculateDiff();
      return this.left;
    }

    public Node getRight() {
      calculateDiff();
      return this.right;
    }

    public T getValue() {
      return this.value;
    }

    public void increase() {
      this.count++;
      this.order++;
    }

    public void decrease() {
      this.count--;
      this.order--;
    }

    public boolean empty() {
      return this.count <= 0;
    }
  }

  Node root = null;
  int size = 0;

  private Node balance(Node cur) {
    if (cur == null) {
      return null;
    }
    int dif = cur.diff();
    if (dif > 1) {
      if ((cur.getLeft()).diff() < 0) {
        cur.setLeft(leftRotate( cur.getLeft()));
      }
      return rightRotate(cur);
    }
    if (dif < -1) {
      if ((cur.getRight()).diff() > 0) {
        cur.setRight(rightRotate( cur.getRight()));
      }
      return leftRotate(cur);
    }
    return cur;
  }


  private void balanceRoot() {
    this.root = balance(this.root);
  }

  private Node leftRotate(Node cur) {
    Node newRoot = cur.getRight();
    cur.setRight(newRoot.getLeft());
    newRoot.setLeft(cur);
    return newRoot;
  }

  private Node rightRotate(Node cur) {
    Node newRoot = cur.getLeft();
    cur.setLeft(newRoot.getRight());
    newRoot.setRight(cur);
    return newRoot;
  }


  @Override
  public void add(T value) {
    if (isEmpty()) {
      this.size++;
      this.root = new Node(value);
      return;
    }
    add(this.root, value);
    balanceRoot();
  }

  protected void add(Node cur, T value) {
    if (cur.value.equals(value)) {
      cur.increase();
    } else if (cur.value.compareTo(value) > 0) {
      if (cur.getLeft() == null) {
        cur.setLeft(new Node(value));
      } else {
        add(cur.getLeft(), value);
        cur.setLeft(balance(cur.getLeft()));
      }
    } else {
      if (cur.getRight() == null) {
        cur.setRight(new Node(value));
      } else {
        add(cur.getRight(), value);
        cur.setRight(balance(cur.getRight()));
      }
    }
  }

  @Override
  public boolean remove(T value) {
    if (get(this.root, value)) {
      this.root = remove(this.root, value);
      balanceRoot();
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
      cur.setLeft(balance(cur.getLeft()));
    } else if (cur.getValue().compareTo(value) < 0) {
      cur.setRight(remove(cur.getRight(), value));
      cur.setRight(balance(cur.getRight()));
    } else {
      cur.decrease();
      this.size--;
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
        cur.setRight(balance(cur.getRight()));
      }
    }
    return cur;
  }

  @Override
  public boolean delete(T value) {
    if (get(this.root, value)) {
      this.root = delete(this.root, value);
      balanceRoot();
      return true;
    }
    return false;
  }

  protected Node delete(Node cur, T value) {
    if (cur == null) {
      return null;
    }
    if (cur.getValue().compareTo(value) > 0) {
      cur.setLeft(remove(cur.getLeft(), value));
      cur.setLeft(balance(cur.getLeft()));
    } else if (cur.getValue().compareTo(value) < 0) {
      cur.setRight(remove(cur.getRight(), value));
      cur.setRight(balance(cur.getRight()));
    } else {
      if (cur.getLeft() == null) {
        return cur.getRight();
      }
      if (cur.getRight() == null) {
        return cur.getLeft();
      }
      Node mn = getMin(cur.getRight());
      this.size -= (int) cur.count;
      cur.value = mn.value;
      cur.count = mn.count;
      cur.setRight(delete(cur.getRight(), cur.getValue()));
      cur.setRight(balance(cur.getRight()));
    }
    return cur;
  }

  @Override
  public T next(T value) {
    if (!isEmpty()) {
      return next(this.root, value);
    }
    return null;
  }

  @Override
  public T previous(T value) {
    if (!isEmpty()) {
      return previous(this.root, value);
    }
    return null;
  }

  @Override
  public int size() {
    return size;
  }

  protected T next(Node cur, T value) {
    if (cur.getValue().equals(value)) {
      return value;
    } else if (cur.getValue().compareTo(value) > 0 && cur.getLeft() != null) {
      T tmp = next(cur.getLeft(), value);
      if (tmp == null || tmp.compareTo(cur.getValue()) > 0) {
        return cur.getValue();
      }
      return tmp;
    } else if (cur.getValue().compareTo(value) < 0 && cur.getRight() != null) {
      return next(cur.getRight(), value);
    }
    if (cur.getValue().compareTo(value) > 0) {
      return cur.getValue();
    }
    return null;
  }

  protected T previous(Node cur, T value) {
    if (cur.getValue().equals(value)) {
      return value;
    } else if (cur.getValue().compareTo(value) > 0 && cur.getLeft() != null) {
      return previous(cur.getLeft(), value);
    } else if (cur.getValue().compareTo(value) < 0 && cur.getRight() != null) {
      T tmp = previous(cur.getRight(), value);
      if (tmp == null || tmp.compareTo(cur.getValue()) < 0) {
        return cur.getValue();
      }
      return tmp;
    }
    if (cur.getValue().compareTo(value) < 0) {
      return cur.getValue();
    }
    return null;
  }

  @Override
  public boolean get(T value) {
    if (!isEmpty()) {
      return get(this.root, value);
    }
    return false;
  }

  protected boolean get(Node cur, T value) {
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
  public T getMin() {
    if (isEmpty()) {
      return null;
    }
    return getMin(this.root).getValue();
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
    return getMax(this.root).getValue();
  }

    protected Node getMax(Node cur) {
    if (cur.getRight() == null) {
      return cur;
    }
    return getMax(cur.getRight());
  }

  public T getAt(int order) {
    if (order < 1 || order > size()) {
      return null;
    }
    return getAt(this.root, order);
  }

  private T getAt(Node cur, int order) {
    if (cur.order - cur.count < order && order <= cur.order) {
      return cur.getValue();
    }
    if (cur.order - cur.count <= order) {
      return getAt(cur.getLeft(), order);
    }
    return getAt(cur.getRight(), order);
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
}
