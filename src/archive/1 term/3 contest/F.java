import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws Exception {
    BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter log = new BufferedWriter(new OutputStreamWriter(System.out));

    StringTokenizer AmountInputTokenizer = new StringTokenizer(buffer.readLine());
    int amount = Integer.parseInt(AmountInputTokenizer.nextToken());

    Treap tree = new Treap(true);
    Long lastOperation = null;

    for (int i = 0; i < amount; ++i) {
      StringTokenizer line = new StringTokenizer(buffer.readLine());
      char operation = line.nextToken().charAt(0);
      if (operation == '+') {
        long value = Long.parseLong(line.nextToken());
        if (lastOperation != null) {
          value = (value + lastOperation) % 1000000000L;
        }
        tree.add(value);
        lastOperation = null;
      } else {
        Long left = Long.parseLong(line.nextToken());
        Long right = Long.parseLong(line.nextToken());

        Long result = tree.sum(left, right);
        lastOperation = result;
        log.write(result + "\n");
      }
    }

    log.flush();
  }

  public static class Treap {
    protected static class Node implements Comparable<Node> {
      Long key;
      int priority;
      Node left;
      Node right;
      Node parent;
      Long pref = 0L;
      Long sum = 0L;

      public Node(Long key, int priority) {
        this.key = key;
        this.priority = priority;
        update();
      }

      private void update() {
        if (right == null && left == null) {
          this.pref = key;
          this.sum = key;
        } else if (right == null) {
          this.pref = key + left.sum;
          this.sum = key + left.sum;
        } else if (left == null) {
          this.pref = key;
          this.sum = key + right.sum;
        } else {
          this.pref = key + left.sum;
          this.sum = key + left.sum + right.sum;
        }
      }

      public void setLeft(Node left) {
        this.left = left;
        update();
      }

      public void setRight(Node right) {
        this.right = right;
        update();
      }

      public void setParent(Node parent) {
        this.parent = parent;
        update();
      }

      public Node getLeft() {
        return this.left;
      }

      public Node getRight() {
        return this.right;
      }

      public Long getValue() {
        return this.key;
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
        return Long.compare(this.getValue().compareTo(o.getValue()), 0);
      }
    }

    Random random = new Random();
    Node root;
    boolean auto;

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

    private Pair<Node> split(Node cur, Long value) {
      if (cur == null) {
        return new Pair<>(null, null);
      }
      if (cur.getValue().compareTo(value) < 0) {
        Pair<Node> tmp = split(cur.getRight(), value);
        if (tmp.first != null) {
          tmp.first.setParent(cur);
        }
        cur.setRight(tmp.first);
        return new Pair<>(cur, tmp.second);
      } else {
        Pair<Node> tmp = split(cur.getLeft(), value);
        if (tmp.first != null) {
          tmp.first.setParent(cur);
        }
        cur.setLeft(tmp.second);
        return new Pair<>(tmp.first, cur);
      }
    }

    public void add(Long value) {
      if (get(value)) {
        return;
      }
      int priority = random.nextInt();
      add(value, priority);
    }

    public void add(Long value, int priority) {
      if (get(value)) {
        return;
      }
      if (isEmpty()) {
        this.root = new Node(value, priority);
        return;
      }
      Pair<Node> tmp = split(this.root, value);
      this.root = merge(tmp.first, merge(tmp.second, new Node(value, priority)));
    }

    public boolean get(Long value) {
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

    public Long sum(Long left, Long right) {
      return sum(this.root, right) - sum(this.root, left - 1);
    }

    private Long sum(Node cur, Long value) {
      if (cur == null) {
        return 0L;
      }
      if (cur.getValue() <= value) {
        return cur.pref + sum(cur.getRight(), value);
      }
      return sum(cur.getLeft(), value);
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
}
