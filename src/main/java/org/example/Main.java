package org.example;

import java.util.Scanner;

public class Main {
  public static <T extends Comparable<T>> T max(T a, T b) {
    return (a.compareTo(b) > 0) ? a : b;
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    AVLSet<Long> tree = new AVLSet<>();

    while (input.hasNext()) {
      String command = input.next();
      Long value = input.nextLong();
      Long result;

      switch (command) {
        case "insert":
          tree.add(value);
          break;
        case "delete":
          tree.remove(value);
          break;
        case "exists":
          System.out.println(tree.get(value));
          break;
        case "next":
          result = tree.next(value);
          if (result == null) {
            System.out.println("none");
            break;
          }
          System.out.println(result);
          break;
        case "prev":
          result = tree.previous(value);
          if (result == null) {
            System.out.println("none");
            break;
          }
          System.out.println(result);
          break;
        default:
          result = tree.getAt((int) (value + 1));
          if (result == null) {
            System.out.println("none");
            break;
          }
          System.out.println(result);
          break;
      }
    }
  }

  public static class AVLSet<T extends Comparable<T>> implements BinarySearchTreeInterface<T> {
    protected class Node {
      Node left = null;
      Node right = null;
      T value;
      int diff = 0;
      int height = 1;
      int order = 0;
      int size = 1;

      public Node(T value) {
        this.value = value;
        calculateDiff();
      }

      public int diff() {
        return diff;
      }

      private void calculateDiff() {
        if (left == null && right == null) {
          this.diff = 0;
          this.height = 1;
          this.order = 1;
          this.size = 1;
        } else if (left == null) {
          this.diff = -right.height;
          this.height = right.height + 1;
          this.order = 1;
          this.size = right.size + 1;
        } else if (right == null) {
          this.diff = left.height;
          this.height = left.height + 1;
          this.order = left.size + 1;
          this.size = left.size + 1;
        } else {
          this.diff = left.height - right.height;
          this.height = max(left.height, right.height) + 1;
          this.order = left.size + 1;
          this.size = left.size + right.size + 1;
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
          cur.setLeft(leftRotate(cur.getLeft()));
        }
        return rightRotate(cur);
      }
      if (dif < -1) {
        if ((cur.getRight()).diff() > 0) {
          cur.setRight(rightRotate(cur.getRight()));
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
      if (add(this.root, value)) {
        this.size++;
        balanceRoot();
      }
    }

    protected boolean add(Node cur, T value) {
      if (cur.value.equals(value)) {
        return false;
      }
      if (cur.value.compareTo(value) > 0) {
        if (cur.getLeft() == null) {
          cur.setLeft(new Node(value));
          return true;
        }
        boolean tmp = add(cur.getLeft(), value);
        cur.setLeft(balance(cur.getLeft()));
        return tmp;
      }
      if (cur.getRight() == null) {
        cur.setRight(new Node(value));
        return true;
      }
      boolean tmp = add(cur.getRight(), value);
      cur.setRight(balance(cur.getRight()));
      return tmp;
    }

    @Override
    public boolean remove(T value) {
      if (get(this.root, value)) {
        this.size--;
        this.root = delete(this.root, value);
        balanceRoot();
        return true;
      }
      return false;
    }

    @Override
    public boolean delete(T value) {
      if (get(this.root, value)) {
        this.size--;
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
        cur.setLeft(delete(cur.getLeft(), value));
        cur.setLeft(balance(cur.getLeft()));
      } else if (cur.getValue().compareTo(value) < 0) {
        cur.setRight(delete(cur.getRight(), value));
        cur.setRight(balance(cur.getRight()));
      } else {
        if (cur.getLeft() == null) {
          return cur.getRight();
        }
        if (cur.getRight() == null) {
          return cur.getLeft();
        }
        Node mn = getMin(cur.getRight());
        cur.value = mn.value;
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
      if (cur.order == order) {
        return cur.getValue();
      }
      if (cur.order > order) {
        return getAt(cur.getLeft(), order);
      }
      return getAt(cur.getRight(), order - cur.order);
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

  public interface BinarySearchTreeInterface<T extends Comparable<T>> {
    void add(T value);

    boolean remove(T value);

    boolean delete(T value);

    boolean get(T value);

    T getMin();

    T getMax();

    boolean isEmpty();

    void clear();

    T next(T value);

    T previous(T value);

    int size();
  }
}
