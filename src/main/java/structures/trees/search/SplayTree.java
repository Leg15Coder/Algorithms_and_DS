package structures.trees.search;

public class SplayTree<T extends Comparable<T>> extends BinarySearchTree<T> {
  private BinarySearchTree<T>.Node splay(BinarySearchTree<T>.Node cur, T value) {
    if (cur == null) {
      return null;
    }

    if (cur.getValue().compareTo(value) > 0) {
      if (cur.getLeft() == null) {
        return cur;
      }
      if (cur.getLeft().getValue().compareTo(value) > 0) {
        cur = zigZig(cur, value, true);
      } else if (cur.getLeft().getValue().compareTo(value) < 0) {
        cur = zigZag(cur, value, true);
      }
      if (cur.getLeft() == null) {
        return cur;
      }
      return zig(cur, false);
    }

    if (cur.getValue().compareTo(value) < 0) {
      if (cur.getRight() == null) {
        return cur;
      }
      if (cur.getRight().getValue().compareTo(value) > 0) {
        cur = zigZag(cur, value, false);
      } else if (cur.getRight().getValue().compareTo(value) < 0) {
        cur = zigZig(cur, value, false);
      }
      if (cur.getRight() == null) {
        return cur;
      }
      return zig(cur, true);
    }

    return cur;
  }

  private BinarySearchTree<T>.Node zig(BinarySearchTree<T>.Node cur, boolean left) {
    if (left) {
      BinarySearchTree<T>.Node tmp = cur.getRight();
      cur.setRight(tmp.getLeft());
      tmp.setLeft(cur);
      return tmp;
    }

    BinarySearchTree<T>.Node tmp = cur.getLeft();
    cur.setLeft(tmp.getRight());
    tmp.setRight(cur);
    return tmp;
  }

  private BinarySearchTree<T>.Node zigZig(BinarySearchTree<T>.Node cur, T value, boolean left) {
    if (left) {
      cur.getLeft().setLeft(splay(cur.getLeft().getLeft(), value));
      return zig(cur, false);
    }

    cur.getRight().setRight(splay(cur.getRight().getRight(), value));
    return zig(cur, true);
  }

  private BinarySearchTree<T>.Node zigZag(BinarySearchTree<T>.Node cur, T value, boolean left) {
    if (left) {
      cur.getLeft().setRight(splay(cur.getLeft().getRight(), value));
      if (cur.getLeft().getRight() != null) {
        cur.setLeft(zig(cur.getLeft(), true));
      }

      return cur;
    }

    cur.getRight().setLeft(splay(cur.getRight().getLeft(), value));
    if (cur.getRight().getLeft() != null) {
      cur.setRight(zig(cur.getRight(), false));
    }

    return cur;
  }

  @Override
  public void add(T value) {
    ++size;

    if (isEmpty()) {
      this.root = new BinarySearchTree<T>.Node(value, null);
      return;
    }

    this.root = splay(this.root, value);
    if (this.root.getValue().equals(value)) {
      this.root.increase();
    } else {
      --size;
      super.add(value);
    }
  }

  @Override
  public boolean remove(T value) {
    if (isEmpty()) {
      return false;
    }

    this.root = splay(this.root, value);

    if (this.root.getValue().equals(value)) {
      this.root.decrease();

      if (this.root.count <= 0) {
        deleteRoot();
      }

      --size;
      return true;
    }

    return false;
  }

  @Override
  public boolean delete(T value) {
    if (isEmpty()) {
      return false;
    }

    this.root = splay(this.root, value);

    if (this.root.getValue().equals(value)) {
      size -= (int) this.root.count;
      deleteRoot();
      return true;
    }

    return false;
  }

  private void deleteRoot() {
    BinarySearchTree<T>.Node tmp = this.root;
    if (this.root.getLeft() == null) {
      this.root = this.root.getRight();
    } else {
      this.root = splay(this.root.getLeft(), this.root.getValue());
      this.root.setRight(tmp.getRight());
    }
  }
}