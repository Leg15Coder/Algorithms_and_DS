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

  public interface Operation<T> {
    T func(T left, T right);
  }

  public static class Sum implements Operation<Long> {
    @Override
    public Long func(Long left, Long right) {
      return left + right;
    }
  }

  public static class Treap<T extends Comparable<T>> {
    protected void updateNode(Treap<T>.Node node) {
      if (node.getLeft() == null && node.getRight() == null) {
        node.index = 0;
        node.subtreesSize = 1;
        node.sum = node.getValue();
      } else if (node.getLeft() == null) {
        node.index = 0;
        node.subtreesSize = 1 + node.getRight().getSize();
        node.sum = this.operation.func(node.getValue(), node.getRight().sum);
      } else if (node.getRight() == null) {
        node.index = node.getLeft().getSize();
        node.subtreesSize = 1 + node.getLeft().getSize();
        node.sum = this.operation.func(node.getValue(), node.getLeft().sum);
      } else {
        node.index = node.getLeft().getSize();
        node.subtreesSize = 1 + node.getLeft().getSize() + node.getRight().getSize();
        node.sum =
            this.operation.func(
                node.getValue(), this.operation.func(node.getLeft().sum, node.getRight().sum));
      }
    }

    protected class Node {
      private final T key;
      private final int priority;
      private Node left;
      private Node right;
      T sum;
      int subtreesSize;
      int index;

      public Node(T key, int priority) {
        this.priority = priority;
        this.key = key;
        updateNode(this);
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

    protected Node root = null;
    private final Random rnd = new Random();
    private final Operation<T> operation;

    public Treap(Operation<T> operation) {
      this.operation = operation;
    }

    private Node merge(Node left, Node right) {
      if (left == null) {
        return right;
      }
      if (right == null) {
        return left;
      }
      if (left.getPriority() > right.getPriority()) {
        left.setRight(merge(left.getRight(), right));
        updateNode(left);
        return left;
      } else {
        right.setLeft(merge(left, right.getLeft()));
        updateNode(right);
        return right;
      }
    }

    private Pair<Node, Node> split(Node cur, int index, boolean isLessOrEq) {
      if (cur == null) {
        return new Pair<>(null, null);
      }
      if (cur.getIndex() < index || isLessOrEq && cur.getIndex() == index) {
        Pair<Node, Node> tmp = split(cur.getRight(), index - cur.getIndex() - 1, isLessOrEq);
        cur.setRight(tmp.first);
        updateNode(cur);
        return new Pair<>(cur, tmp.second);
      } else {
        Pair<Node, Node> tmp = split(cur.getLeft(), index, isLessOrEq);
        cur.setLeft(tmp.second);
        updateNode(cur);
        return new Pair<>(tmp.first, cur);
      }
    }

    public void insert(T value, int index) {
      if (index > getSize()) {
        throw new IllegalArgumentException("Нет такого индекса");
      }
      index--;
      if (index < 0) {
        this.root = merge(new Node(value, rnd.nextInt()), this.root);
        return;
      }
      Pair<Treap<T>.Node, Treap<T>.Node> tmp = split(this.root, index, true);
      this.root = merge(merge(tmp.first, new Node(value, rnd.nextInt())), tmp.second);
    }

    public Pair<Pair<Node, Node>, Node> split(int left, int right) {
      Pair<Node, Node> less = split(this.root, right - 1, true);
      Pair<Node, Node> more = split(less.first, left - 1, false);
      return new Pair<>(more, less.second);
    }

    public T get(int index) {
      index--;
      Node cur = this.root;
      while (cur != null) {
        if (cur.getIndex() == index) {
          return cur.getValue();
        } else if (cur.getIndex() > index) {
          cur = cur.getLeft();
        } else {
          index -= cur.getIndex() + 1;
          cur = cur.getRight();
        }
      }
      return null;
    }

    public void mergeSegments(Pair<Pair<Node, Node>, Node> partitions) {
      this.root = merge(merge(partitions.first.first, partitions.first.second), partitions.second);
      updateNode(this.root);
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
      return toStr(this.root);
    }

    public String toStr(Node cur) {
      if (cur == null) return "";
      return toStr(cur.getLeft()) + " " + cur.getValue() + " " + toStr(cur.getRight());
    }
  }

  public static class TreapsManager {
    Treap<Long> odd = new Treap<>(new Sum());
    Treap<Long> even = new Treap<>(new Sum());

    public TreapsManager(Long[] arr) {
      for (int i = 0; i < arr.length; ++i) {
        if (i % 2 == 0) {
          odd.insert(arr[i], odd.getSize());
        } else {
          even.insert(arr[i], even.getSize());
        }
      }
    }

    public void swap(int left, int right) {
      var oddPartitions = odd.split((left + 2) / 2, (right + 1) / 2);
      var evenPartitions = even.split((left + 1) / 2, right / 2);

      Treap<Long>.Node tmp = oddPartitions.first.second;
      oddPartitions.first.second = evenPartitions.first.second;
      evenPartitions.first.second = tmp;

      odd.mergeSegments(oddPartitions);
      even.mergeSegments(evenPartitions);

      // System.out.println(odd);
      // System.out.println(even);
    }

    public Long sum(int left, int right) {
      if (right - left == 0) {
        if (left % 2 == 0) {
          return even.get(left / 2);
        }
        return odd.get((left + 1) / 2);
      }
      var oddPartitions = odd.split((left + 2) / 2, (right + 1) / 2);
      var evenPartitions = even.split((left + 1) / 2, right / 2);

      // System.out.println(odd.toStr(oddPartitions.first.second));
      // System.out.println(even.toStr(evenPartitions.first.second));

      Long result = 0L;
      if (oddPartitions.first.second != null) {
        result += oddPartitions.first.second.sum;
      }
      if (evenPartitions.first.second != null) {
        result += evenPartitions.first.second.sum;
      }

      odd.mergeSegments(oddPartitions);
      even.mergeSegments(evenPartitions);

      return result;
    }
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    int counter = 0;

    while (true) {
      counter++;
      int n = input.nextInt();
      int m = input.nextInt();

      if (n == 0 && m == 0) {
        break;
      }
      System.out.println("Swapper " + counter + ":");

      Long[] arr = new Long[n];

      for (int i = 0; i < n; ++i) {
        arr[i] = input.nextLong();
      }

      TreapsManager manager = new TreapsManager(arr);

      for (int i = 0; i < m; ++i) {
        int command = input.nextInt();

        if (command == 1) {
          int x = input.nextInt();
          int y = input.nextInt();

          manager.swap(x, y);
        } else {
          int a = input.nextInt();
          int b = input.nextInt();

          System.out.println(manager.sum(a, b));
        }
      }
    }
  }
}
