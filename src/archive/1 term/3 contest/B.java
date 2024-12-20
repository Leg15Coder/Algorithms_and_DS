import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

public class Main {
  public static <T extends Comparable<T>> T max(T a, T b) {
    return (a.compareTo(b) > 0) ? a : b;
  }

  public static void main(String[] args) throws Exception {
    BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter log = new BufferedWriter(new OutputStreamWriter(System.out));

    StringTokenizer AmountInputTokenizer = new StringTokenizer(buffer.readLine());
    int amount = Integer.parseInt(AmountInputTokenizer.nextToken());

    Treap<Integer> tree = new Treap<>(false);
    Integer[] numbers = new Integer[amount];
    int[] priorities = new int[amount];

    for (int i = 0; i < amount; ++i) {
      StringTokenizer line = new StringTokenizer(buffer.readLine());
      numbers[i] = Integer.parseInt(line.nextToken());
      priorities[i] = Integer.parseInt(line.nextToken());
    }
    ArrayList<Treap<Integer>.Node> toPrint = tree.build(numbers, priorities);

    log.write("YES\n");
    for (var node : toPrint) {
      if (node.getParent() == null) {
        log.write("0 ");
      } else {
        log.write(node.getParent().getIndex() + " ");
      }
      if (node.getLeft() != null) {
        log.write(node.getLeft().getIndex() + " ");
      } else {
        log.write("0 ");
      }
      if (node.getRight() != null) {
        log.write(node.getRight().getIndex() + "\n");
      } else {
        log.write("0\n");
      }
    }
    log.flush();
  }

  public static class Treap<T extends Comparable<T>> implements BinarySearchTreeInterface<T> {
    protected class Node implements Comparable<Node> {
      T key;
      int priority;
      Node left;
      Node right;
      Node parent;
      int index;

      public Node(T key, int priority, int index) {
        this.key = key;
        this.priority = priority;
        this.index = index;
      }

      public void setLeft(Node left) {
        this.left = left;
      }

      public void setRight(Node right) {
        this.right = right;
      }

      public void setParent(Node parent) {
        this.parent = parent;
      }

      public Node getLeft() {
        return this.left;
      }

      public Node getRight() {
        return this.right;
      }

      public Node getParent() {
        return this.parent;
      }

      public T getValue() {
        return this.key;
      }

      public int getIndex() {
        return this.index;
      }

      public int getPriority() {
        return this.priority;
      }

      @Override
      public int compareTo(Node o) {
        if (this.getPriority() > o.getPriority()) {
          return 1;
        }
        if (this.getPriority() < o.getPriority()) {
          return -1;
        }
        if (this.getValue().compareTo(o.getValue()) > 0) {
          return 1;
        }
        if (this.getValue().compareTo(o.getValue()) < 0) {
          return -1;
        }
        return 0;
      }

      public String toString(Node parent) {
        String leftResult = "";
        String rightResult = "";
        String cur = "";
        if (parent == null) {
          cur += "0 ";
        } else {
          cur += parent.getIndex() + " ";
        }
        if (this.getLeft() != null) {
          leftResult += this.getLeft().toString(this);
          cur += this.getLeft().getIndex() + " ";
        } else {
          cur += "0 ";
        }
        if (this.getRight() != null) {
          rightResult += this.getRight().toString(this);
          cur += this.getRight().getIndex() + "\n";
        } else {
          cur += "0\n";
        }
        return leftResult + cur + rightResult;
      }
    }

    Random random = new Random();
    Node root;
    boolean auto;

    public Treap() {
      this.root = null;
      this.auto = true;
    }

    public Treap(boolean isAutoPriorities) {
      this.root = null;
      this.auto = isAutoPriorities;
    }

    private Node merge(Node left, Node right) {
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
        Node tmp = merge(left.getLeft(), right);
        tmp.setParent(left);
        left.setLeft(tmp);
      } else {
        Node tmp = merge(left.getRight(), right);
        tmp.setParent(left);
        left.setRight(tmp);
      }
      return left;
    }

    private Pair<Node> split(Node cur, T value) {
      if (cur == null) {
        return new Pair<>(null, null);
      }
      if (cur.getValue().compareTo(value) < 0) {
        Pair<Node> tmp = split(cur.getRight(), value);
        tmp.first.setParent(cur);
        cur.setRight(tmp.first);
        return new Pair<>(cur, tmp.second);
      } else {
        Pair<Node> tmp = split(cur.getLeft(), value);
        tmp.first.setParent(cur);
        cur.setLeft(tmp.second);
        return new Pair<>(tmp.first, cur);
      }
    }

    @Override
    public void add(T value) {
      int priority = random.nextInt();
      add(value, priority, 0);
    }

    public void add(T value, int priority, int index) {
      if (isEmpty()) {
        this.root = new Node(value, priority, index);
        return;
      }
      Pair<Node> tmp = split(this.root, value);
      this.root = merge(tmp.first, merge(tmp.second, new Node(value, priority, index)));
    }

    public ArrayList<Node> build(T[] values, int[] priorities) {
      Node last = null;
      ArrayList<Node> result = new ArrayList<>();
      for (int i = 0; i < values.length; ++i) {
        Node cur = last;
        if (cur == null) {
          this.root = new Node(values[i], priorities[i], i + 1);
          last = this.root;
          result.add(this.root);
          continue;
        }
        while (cur != null && priorities[i] < cur.getPriority()) {
          cur = cur.getParent();
        }
        if (cur == null) {
          Node newRoot = new Node(values[i], priorities[i], i + 1);
          newRoot.setLeft(this.root);
          this.root.setParent(newRoot);
          this.root = newRoot;
          last = newRoot;
          result.add(newRoot);
        } else {
          Node newNode = new Node(values[i], priorities[i], i + 1);
          newNode.setLeft(cur.getRight());
          if (cur.getRight() != null) {
            cur.getRight().setParent(newNode);
          }
          cur.setRight(newNode);
          newNode.setParent(cur);
          last = newNode;
          result.add(newNode);
        }
      }
      return result;
    }

    @Override
    public boolean remove(T value) {
      if (get(value)) {
        Pair<Node> tmp = split(this.root, value);
        this.root = merge(tmp.first, merge(tmp.second.getLeft(), tmp.second.getRight()));
        return true;
      }
      return false;
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
      Node cur = this.root;
      while (cur.getLeft() != null) {
        cur = cur.getLeft();
      }
      return cur.getValue();
    }

    @Override
    public T getMax() {
      if (isEmpty()) {
        return null;
      }
      Node cur = this.root;
      while (cur.getRight() != null) {
        cur = cur.getRight();
      }
      return cur.getValue();
    }

    @Override
    public boolean isEmpty() {
      return root == null;
    }

    @Override
    public void clear() {
      this.root = null;
    }

    @Override
    public String toString() {
      if (isEmpty()) {
        return "null";
      }
      return this.root.toString(null);
    }
  }

  public static class Pair<T> {
    public T first;
    public T second;

    public Pair(T first, T second) {
      this.first = first;
      this.second = second;
    }
  }

  public interface BinarySearchTreeInterface<T extends Comparable<T>> {
    void add(T value);

    boolean remove(T value);

    boolean get(T value);

    T getMin();

    T getMax();

    boolean isEmpty();

    void clear();
  }
}
