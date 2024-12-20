import java.util.Random;
import java.util.Scanner;

public class Main {
  public static <T extends Comparable<T>> T min(T l, T r) {
    if (l == null & r == null) {
      return null;
    }
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
    protected void updateNode(Treap<T>.Node node) {
      if (node.getLeft() == null && node.getRight() == null) {
        node.index = 0;
        node.subtreesSize = node.count;
      } else if (node.getLeft() == null) {
        node.index = 0;
        node.subtreesSize = node.count + node.getRight().getSize();
      } else if (node.getRight() == null) {
        node.index = node.getLeft().getSize();
        node.subtreesSize = node.count + node.getLeft().getSize();
      } else {
        node.index = node.getLeft().getSize();
        node.subtreesSize = node.count + node.getLeft().getSize() + node.getRight().getSize();
      }
    }

    protected class Node implements Comparable<Node> {
      T key;
      int priority;
      Node left;
      Node right;
      int subtreesSize;
      int index;
      int count = 1;

      public Node(T key, int priority) {
        this.key = key;
        this.priority = priority;
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

      @Override
      public int compareTo(Node o) {
        if (this.getPriority() > o.getPriority()) {
          return 1;
        }
        if (this.getPriority() < o.getPriority()) {
          return -1;
        }
        return Integer.compare(this.getValue().compareTo(o.getValue()), 0);
      }
    }

    protected Random random = new Random();
    protected Node root = null;

    protected Node merge(Node left, Node right) {
      if (left == null) {
        return right;
      }
      if (right == null) {
        return left;
      }
      if (left.compareTo(right) > 0) {
        Node tmp = left;
        left = right;
        right = tmp;
      }
      if (left.getValue().compareTo(right.getValue()) > 0) {
        left.setLeft(merge(left.getLeft(), right));
      } else {
        left.setRight(merge(left.getRight(), right));
      }
      return left;
    }

    protected Pair<Node, Node> splitLessOrEq(Node cur, T value) {
      if (cur == null) {
        return new Pair<>(null, null);
      }
      if (cur.getValue().compareTo(value) > 0) {
        Pair<Node, Node> tmp = splitLessOrEq(cur.getLeft(), value);
        cur.setLeft(tmp.second);
        return new Pair<>(tmp.first, cur);
      } else {
        Pair<Node, Node> tmp = splitLessOrEq(cur.getRight(), value);
        cur.setRight(tmp.first);
        return new Pair<>(cur, tmp.second);
      }
    }

    protected Pair<Node, Node> splitMoreOrEq(Node cur, T value) {
      if (cur == null) {
        return new Pair<>(null, null);
      }
      if (cur.getValue().compareTo(value) < 0) {
        Pair<Node, Node> tmp = splitMoreOrEq(cur.getRight(), value);
        cur.setRight(tmp.first);
        return new Pair<>(cur, tmp.second);
      } else {
        Pair<Node, Node> tmp = splitMoreOrEq(cur.getLeft(), value);
        cur.setLeft(tmp.second);
        return new Pair<>(tmp.first, cur);
      }
    }

    public void add(T value) {
      int priority = random.nextInt();
      add(value, priority);
    }

    public void add(T value, int priority) {
      if (isEmpty()) {
        this.root = new Treap<T>.Node(value, priority);
        return;
      }
      if (get(value)) {
        add(this.root, value);
        return;
      }
      Pair<Treap<T>.Node, Treap<T>.Node> tmp = splitMoreOrEq(this.root, value);
      this.root = merge(tmp.first, merge(tmp.second, new Treap<T>.Node(value, priority)));
    }

    public void add(Node cur, T value) {
      if (cur.getValue().equals(value)) {
        cur.count++;
        updateNode(cur);
        return;
      }
      if (cur.getValue().compareTo(value) > 0) {
        add(cur.getLeft(), value);
        updateNode(cur);
        return;
      }
      add(cur.getRight(), value);
      updateNode(cur);
    }

    public void remove(T value) {
      if (get(value)) {
        Pair<Node, Node> less = splitMoreOrEq(this.root, value);
        Pair<Node, Node> more = splitLessOrEq(less.second, value);
        more.first.count--;
        if (more.first.count == 0) {
          more.first = null;
        } else {
          updateNode(more.first);
        }
        this.root = merge(less.first, merge(more.first, more.second));
      }
    }

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

    public boolean isEmpty() {
      return root == null;
    }

    public int getIndex(T value) {
      if (!get(value)) {
        throw new IllegalArgumentException(
            "Невозможно найти индекс: данного элемента нет в массиве");
      }
      return getIndex(this.root, value);
    }

    private int getIndex(Treap<T>.Node cur, T value) {
      if (cur.getValue().equals(value)) {
        return cur.getIndex();
      }
      if (cur.getValue().compareTo(value) > 0) {
        return getIndex(cur.getLeft(), value);
      }
      return cur.getIndex() + getIndex(cur.getRight(), value) + cur.count;
    }

    public int getSize() {
      if (isEmpty()) {
        return 0;
      }
      return this.root.getSize();
    }
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    int queries = input.nextInt();

    Treap<Integer> tree = new Treap<>();
    Integer[] runners = new Integer[100002];

    for (int i = 0; i < queries; ++i) {
      String command = input.next();
      int user = input.nextInt();

      if (command.equals("RUN")) {
        Integer page = input.nextInt();

        if (runners[user] != null) {
          tree.remove(runners[user]);
        }
        runners[user] = page;
        tree.add(page);
      } else {
        if (runners[user] == null) {
          System.out.println(0.0);
        } else {
          if (tree.getSize() == 1) {
            System.out.println(1.0);
            continue;
          }

          double index = tree.getIndex(runners[user]);
          System.out.println(index / (tree.getSize() - 1));
        }
      }
    }
  }
}
