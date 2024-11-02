package structures.trees.search;

public class SplayTree<T extends Comparable<T>> implements BinarySearchTreeInterface<T> {
  protected class Node {
    Node left = null;
    Node right = null;
    T value;
    T key;

    public Node(T key, T value) {
      this.key = key;
      this.value = value;
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
      return this.key;
    }

    public T getValueFromKey() {
      return this.value;
    }
  }

  Node root = null;

  private Node splay(Node cur, T key) {
    if (cur == null) {
      return null;
    }
    if (cur.getValue().compareTo(key) > 0) {
      if (cur.getLeft() == null) {
        return cur;
      }
      if (cur.getLeft().getValue().compareTo(key) > 0) {
        cur = zigZig(cur, key, true);
      } else if (cur.getLeft().getValue().compareTo(key) < 0) {
        cur = zigZag(cur, key, true);
      }
      if (cur.getLeft() == null) {
        return cur;
      }
      return zig(cur, false);
    }
    if (cur.getValue().compareTo(key) < 0) {
      if (cur.getRight() == null) {
        return cur;
      }
      if (cur.getRight().getValue().compareTo(key) > 0) {
        cur = zigZag(cur, key, false);
      } else if (cur.getRight().getValue().compareTo(key) < 0) {
        cur = zigZig(cur, key, false);
      }
      if (cur.getRight() == null) {
        return cur;
      }
      return zig(cur, true);
    }
    return cur;
  }

  private Node zig(Node cur, boolean left) {
    if (left) {
      Node tmp = cur.getRight();
      cur.setRight(tmp.getLeft());
      tmp.setLeft(cur);
      return tmp;
    }
    Node tmp = cur.getLeft();
    cur.setLeft(tmp.getRight());
    tmp.setRight(cur);
    return tmp;
  }

  private Node zigZig(Node cur, T key, boolean left) {
    if (left) {
      cur.getLeft().setLeft(splay(cur.getLeft().getLeft(), key));
      return zig(cur, false);
    }
    cur.getRight().setRight(splay(cur.getRight().getRight(), key));
    return zig(cur, true);
  }

  private Node zigZag(Node cur, T key, boolean left) {
    if (left) {
      cur.getLeft().setRight(splay(cur.getLeft().getRight(), key));
      if (cur.getLeft().getRight() != null) {
        cur.setLeft(zig(cur.getLeft(), true));
      }
      return cur;
    }
    cur.getRight().setLeft(splay(cur.getRight().getLeft(), key));
    if (cur.getRight().getLeft() != null) {
      cur.setRight(zig(cur.getRight(), false));
    }
    return cur;
  }

  public void add(T key, T value) {
    Node newNode = new Node(key, value);
    this.root = splay(this.root, key);

    if (isEmpty()) {
      this.root = newNode;
      return;
    }

    if (key.compareTo(root.getValue()) < 0) {
      newNode.setLeft(root.getLeft());
      newNode.setRight(root);
      this.root.setLeft(null);
    } else if (key.compareTo(root.getValue()) > 0) {
      newNode.setRight(root.getRight());
      newNode.setLeft(root);
      this.root.setRight(null);
    } else {
      newNode.setLeft(root.getLeft());
      newNode.setRight(root.getRight());
    }
    this.root = newNode;
  }

  @Override
  public void add(T value) {
    throw new IllegalCallerException("Not right add");
  }

  @Override
  public boolean remove(T value) {
    this.root = splay(this.root, value);
    if (isEmpty() || !this.root.getValue().equals(value)) {
      return false;
    }
    if (root.getLeft() == null) {
      this.root = this.root.getRight();
    } else {
      Node right = root.getRight();
      this.root = root.getLeft();
      this.root = splay(this.root, getMax(root).getValue());
      this.root.setRight(right);
    }
    return true;
  }

  @Override
  public boolean get(T value) {
    return getAt(value) != null;
  }

  public T getAt(T value) {
    this.root = splay(this.root, value);
    if (!isEmpty()) {
      return this.root.getValueFromKey();
    }
    return null;
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

  @Override
  public boolean isEmpty() {
    return root == null;
  }

  @Override
  public void clear() {
    this.root = null;
  }
}