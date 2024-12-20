import java.util.Random;
import java.util.Scanner;

public class Main {
  public static <T extends Comparable<T>> T min(T l, T r) {
    if (l == null) {
      return r;
    }
    if (r == null) {
      return l;
    }
    return (l.compareTo(r) > 0) ? r : l;
  }

  public static class Pair<T, E> {
    public T first;
    public E second;

    public Pair(T first, E second) {
      this.first = first;
      this.second = second;
    }
  }

  public static class Treap<T extends Comparable<T>> {
    protected Node root = null;
    private final Random rnd = new Random();

    protected class Node {
      private final T key;
      private final int priority;
      private Node left;
      private Node right;
      T min;
      int subtreesSize;
      int index;

      public Node(T key, int priority) {
        this.priority = priority;
        this.key = key;
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
        return this.key;
      }

      public int getPriority() {
        return this.priority;
      }

      public int getSize() {
        return this.subtreesSize;
      }

      public int getIndex() {
        return this.index;
      }
    }

    protected void updateNode(Node node) {
      if (node == null) return;
      if (node.getLeft() == null && node.getRight() == null) {
        node.index = 0;
        node.subtreesSize = 1;
        node.min = node.getValue();
      } else if (node.getLeft() == null) {
        node.index = 0;
        node.subtreesSize = 1 + node.getRight().getSize();
        node.min = min(node.getValue(), node.getRight().min);
      } else if (node.getRight() == null) {
        node.index = node.getLeft().getSize();
        node.subtreesSize = 1 + node.getLeft().getSize();
        node.min = min(node.getValue(), node.getLeft().min);
      } else {
        node.index = node.getLeft().getSize();
        node.subtreesSize = 1 + node.getLeft().getSize() + node.getRight().getSize();
        node.min = min(node.getValue(), min(node.getLeft().min, node.getRight().min));
      }
    }

    protected Node merge(Node left, Node right) {
      if (left == null) {
        return right;
      }

      if (right == null) {
        return left;
      }

      if (left.getPriority() > right.getPriority()) {
        left.setRight(merge(left.getRight(), right));
        return left;
      }

      right.setLeft(merge(left, right.getLeft()));
      return right;
    }

    protected Pair<Node, Node> split(Node cur, int index, boolean isLessOrEq) {
      if (cur == null) {
        return new Pair<>(null, null);
      }
      if (cur.getIndex() < index || isLessOrEq && cur.getIndex() == index) {
        Pair<Node, Node> tmp = split(cur.getRight(), index - cur.getIndex() - 1, isLessOrEq);
        cur.setRight(tmp.first);
        return new Pair<>(cur, tmp.second);
      } else {
        Pair<Node, Node> tmp = split(cur.getLeft(), index, isLessOrEq);
        cur.setLeft(tmp.second);
        return new Pair<>(tmp.first, cur);
      }
    }

    public void insert(T value, int index) {
      if (index > getSize()) {
        throw new IllegalArgumentException("Invalid index.");
      }
      index--;
      if (index < 0) {
        this.root = merge(new Node(value, rnd.nextInt()), this.root);
        updateNode(this.root);
        return;
      }
      Pair<Node, Node> tmp = split(this.root, index, true);
      this.root = merge(merge(tmp.first, new Node(value, rnd.nextInt())), tmp.second);
      updateNode(this.root);
    }

    public T getMin(int left, int right) {
      left--;
      right--;
      Pair<Node, Node> less = split(this.root, right, true);
      Pair<Node, Node> more = split(less.first, left, false);
      T result = more.second.min;
      // System.out.println("! " + toStr(more.second));
      this.root = merge(merge(more.first, more.second), less.second);
      updateNode(this.root);
      return result;
    }

    public boolean isEmpty() {
      return root == null;
    }

    public int getSize() {
      if (isEmpty()) {
        return 0;
      }
      return this.root.getSize();
    }

    @Override
    public String toString() {
      if (isEmpty()) {
        return "null";
      }
      return (this.root.getIndex() + 1) + ")" + toStr(this.root);
    }

    public String toStr(Node cur) {
      if (cur == null) return "";
      return toStr(cur.getLeft()) + " " + cur.getValue() + " " + toStr(cur.getRight());
    }
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    int countOperations = input.nextInt();
    Treap<Long> tree = new Treap<>();

    for (int i = 0; i < countOperations; ++i) {
      String operation = input.next();

      if (operation.equals("+")) {
        int index = input.nextInt();
        Long value = input.nextLong();

        tree.insert(value, index);
        // System.out.println(tree);
      } else { // operation "?"
        int left = input.nextInt();
        int right = input.nextInt();

        System.out.println(tree.getMin(left, right));
      }
    }
  }
}
