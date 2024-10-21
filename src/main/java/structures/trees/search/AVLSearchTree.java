package structures.trees.search;

import static utils.Compare.max;

public class AVLSearchTree<T extends Comparable<T>> extends BinarySearchTree<T> implements AVLSearchTreeInterface<T> {
  private class Node extends BinarySearchTree<T>.Node {
    int diff = 0;
    int height = 1;
    Node left;
    Node right;

    public Node(T value) {
      super(value);
      calculateDiff();
    }

    public int diff() {
      return diff;
    }

    private void calculateDiff() {
      if (left == null && right == null) {
        this.diff = 0;
        this.height = 1;
      } else if (left == null) {
        this.diff = -right.height;
        this.height = right.height + 1;
      } else if (right == null) {
        this.diff = left.height;
        this.height = left.height + 1;
      } else {
        this.diff = left.height - right.height;
        this.height = max(left.height, right.height) + 1;
      }
    }

    @Override
    public void setRight(BinarySearchTree<T>.Node right) {
      this.right = (Node) right;
      super.setRight(right);
      calculateDiff();
    }

    @Override
    public void setLeft(BinarySearchTree<T>.Node left) {
      this.left = (Node) left;
      super.setLeft(left);
      calculateDiff();
    }

    @Override
    public Node getLeft() {
      return (Node) super.getLeft();
    }

    @Override
    public Node getRight() {
      return (Node) super.getRight();
    }
  }

  Node root = null;

  @Override
  public T next(T value) {
    return next((Node) this.root, value);
  }

  private T next(Node cur, T value) {
    if (cur == null) {
      return null;
    }
    if (cur.value.compareTo(value) > 0 && cur.getLeft() != null) {
      Node tmp = (Node) next((Node) cur.getLeft(), value);
      if (tmp == null) {
        return cur.getValue();
      }
      return tmp.getValue();
    }
    if (cur.value.compareTo(value) < 0 && cur.getRight() != null) {
      return next((Node) cur.getRight(), value);
    }
    return null;
  }

  private Node balance(Node cur) {
    if (cur == null) {
      return null;
    }
    if (cur.diff() == -1) {
      return leftRotate(cur);
    } else if (cur.diff() == 1) {
      return rightRotate(cur);
    } else if (cur.diff() == -2) {
      return bigLeftRotate(cur);
    } else if (cur.diff() == 2) {
      return bigRightRotate(cur);
    }
    return null;
  }

  private void balanceChildren(Node cur) {
    cur.setLeft(balance((Node) cur.getLeft()));
    cur.setRight(balance((Node) cur.getRight()));
  }

  private void balanceRoot() {
    this.root = balance((Node) this.root);
  }

  private Node leftRotate(Node cur) {
    Node x = (Node) cur.getRight();
    Node a = (Node) cur.getLeft();
    Node b = (Node) x.getLeft();
    Node c = (Node) x.getRight();
    cur.setLeft(a);
    cur.setRight(b);
    x.setLeft(cur);
    x.setRight(c);
    return x;
  }

  private Node rightRotate(Node cur) {
    Node x = (Node) cur.getLeft();
    Node a = (Node) x.getLeft();
    Node b = (Node) x.getRight();
    Node c = (Node) cur.getRight();
    x.setLeft(a);
    x.setRight(cur);
    cur.setLeft(b);
    cur.setRight(c);
    return x;
  }

  private Node bigLeftRotate(Node cur) {
    Node x = (Node) cur.getRight();
    Node y = (Node) x.getLeft();
    Node a = (Node) cur.getLeft();
    Node b = (Node) y.getLeft();
    Node c = (Node) y.getRight();
    Node d = (Node) x.getRight();
    y.setLeft(cur);
    y.setRight(x);
    cur.setLeft(a);
    cur.setRight(b);
    x.setLeft(c);
    x.setRight(d);
    return y;
  }

  private Node bigRightRotate(Node cur) {
    Node x = (Node) cur.getLeft();
    Node y = (Node) x.getRight();
    Node a = (Node) x.getLeft();
    Node b = (Node) y.getLeft();
    Node c = (Node) y.getRight();
    Node d = (Node) cur.getRight();
    y.setRight(cur);
    y.setLeft(x);
    cur.setLeft(c);
    cur.setRight(d);
    x.setLeft(a);
    x.setRight(b);
    return y;
  }

  @Override
  public void add(T value) {
    if (isEmpty()) {
      this.root = new Node(value);
      // balanceRoot();
      return;
    }
    add(this.root, value);
  }

  @Override
  protected void add(BinarySearchTree<T>.Node cur, T value) {
    super.add(cur, value);
    // balanceChildren((Node) cur);
  }

  @Override
  public boolean remove(T value) {
    if (get(this.root, value)) {
      this.root = (Node) remove(this.root, value);
      // balanceRoot();
      return true;
    }
    return false;
  }

  @Override
  protected BinarySearchTree<T>.Node remove(BinarySearchTree<T>.Node cur, T value) {
    BinarySearchTree<T>.Node tmp = super.remove(cur, value);
    // balanceChildren((Node) cur);
    return tmp;
  }

  @Override
  protected BinarySearchTree<T>.Node delete(BinarySearchTree<T>.Node cur, T value) {
    BinarySearchTree<T>.Node tmp = super.delete(cur, value);
    // balanceChildren((Node) cur);
    return tmp;
  }
}
