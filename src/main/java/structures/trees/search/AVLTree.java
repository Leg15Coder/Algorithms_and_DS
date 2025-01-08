package structures.trees.search;

import static utils.Compare.max;

public class AVLTree<T extends Comparable<T>> extends BinarySearchTree<T> {
  @Override
  protected void updateNode(BinarySearchTree<T>.Node node) {
    if (node.getLeft() == null && node.getRight() == null) {
      ((Node) node.getSubNode()).diff = 0;
      ((Node) node.getSubNode()).height = 1;
    } else if (node.getLeft() == null) {
      ((Node) node.getSubNode()).diff = -((Node) node.getRight().getSubNode()).height;
      ((Node) node.getSubNode()).height = ((Node) node.getRight().getSubNode()).height + 1;
    } else if (node.getRight() == null) {
      ((Node) node.getSubNode()).diff = ((Node) node.getLeft().getSubNode()).height;
      ((Node) node.getSubNode()).height = ((Node) node.getLeft().getSubNode()).height + 1;
    } else {
      ((Node) node.getSubNode()).diff = ((Node) node.getLeft().getSubNode()).height - ((Node) node.getRight().getSubNode()).height;
      ((Node) node.getSubNode()).height = max(((Node) node.getLeft().getSubNode()).height, ((Node) node.getRight().getSubNode()).height) + 1;
    }
  }

  private static class Node {
    int diff = 0;
    int height = 1;

    public int diff() {
      return diff;
    }
  }

  private BinarySearchTree<T>.Node balance(BinarySearchTree<T>.Node cur) {
    if (cur == null) {
      return null;
    }

    int dif = ((Node) cur.getSubNode()).diff();
    if (dif > 1) {
      if (((Node) cur.getLeft().getSubNode()).diff() < 0) {
        cur.setLeft(leftRotate(cur.getLeft()));
      }
      return rightRotate(cur);
    }

    if (dif < -1) {
      if (((Node) cur.getRight().getSubNode()).diff() > 0) {
        cur.setRight(rightRotate(cur.getRight()));
      }
      return leftRotate(cur);
    }

    updateNode(cur);
    return cur;
  }

  private BinarySearchTree<T>.Node leftRotate(BinarySearchTree<T>.Node cur) {
    BinarySearchTree<T>.Node newRoot = cur.getRight();
    cur.setRight(newRoot.getLeft());
    newRoot.setLeft(cur);
    return newRoot;
  }

  private BinarySearchTree<T>.Node rightRotate(BinarySearchTree<T>.Node cur) {
    BinarySearchTree<T>.Node newRoot = cur.getLeft();
    cur.setLeft(newRoot.getRight());
    newRoot.setRight(cur);
    return newRoot;
  }

  @Override
  public void add(T value) {
    ++size;
    if (isEmpty()) {
      this.root = new BinarySearchTree<T>.Node(value, new Node());
      return;
    }

    this.root = balance(add(this.root, value));
  }

  private BinarySearchTree<T>.Node add(BinarySearchTree<T>.Node cur, T value) {
    if (cur.getValue().equals(value)) {
      cur.increase();
    } else if (cur.getValue().compareTo(value) > 0) {
      if (cur.getLeft() == null) {
        cur.setLeft(new BinarySearchTree<T>.Node(value, new Node()));
      } else {
        add(cur.getLeft(), value);
      }
    } else {
      if (cur.getRight() == null) {
        cur.setRight(new BinarySearchTree<T>.Node(value, new Node()));
      } else {
        add(cur.getRight(), value);
      }
    }

    return cur;
  }

  @Override
  protected BinarySearchTree<T>.Node remove(BinarySearchTree<T>.Node cur, T value) {
    return balance(super.remove(cur, value));
  }

  @Override
  protected BinarySearchTree<T>.Node delete(BinarySearchTree<T>.Node cur, T value) {
    return balance(super.delete(cur, value));
  }
}