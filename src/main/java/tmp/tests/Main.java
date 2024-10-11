package tmp.tests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class Main {
  public static <T extends Comparable<T>> T max(T a, T b) {
    return (a.compareTo(b) > 0) ? a : b;
  }

  public static void main(String[] args) throws Exception {
    BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));

    StringTokenizer XYZInputTokenizer = new StringTokenizer(buffer.readLine());
    StringTokenizer LWHInputTokenizer = new StringTokenizer(buffer.readLine());
    int x = Integer.parseInt(XYZInputTokenizer.nextToken());
    int y = Integer.parseInt(XYZInputTokenizer.nextToken());
    int z = Integer.parseInt(XYZInputTokenizer.nextToken());
    int l = Integer.parseInt(LWHInputTokenizer.nextToken());
    int w = Integer.parseInt(LWHInputTokenizer.nextToken());
    int h = Integer.parseInt(LWHInputTokenizer.nextToken());

    Point[][][] ships = new Point[x][y][z];
    Point[][][] maxs = new Point[x][y][z];

    for (int i = 0; i < x; ++i) {
      for (int j = 0; j < y; ++j) {
        StringTokenizer line = new StringTokenizer(buffer.readLine());
        for (int k = 0; k < z; ++k) {
          ships[i][j][k] = new Point(i, j, k, Long.parseLong(line.nextToken()));
        }
      }
    }

    for (int i = 0; i < x; ++i) {
      for (int j = 0; j < y; ++j) {
        QueueWithMax<Point> segment = new QueueWithMax<Point>(h);
        for (int k = z - 1; k >= 0; --k) {
          segment.pushFront(ships[i][j][k]);
          maxs[i][j][k] = segment.getMax();
          if (z - k >= h) {
            segment.popBack();
          }
        }
      }
    }

    for (int k = 0; k < z; ++k) {
      for (int j = 0; j < y; ++j) {
        QueueWithMax<Point> segment = new QueueWithMax<Point>(l);
        for (int i = x - 1; i >= 0; --i) {
          segment.pushFront(maxs[i][j][k]);
          maxs[i][j][k] = segment.getMax();
          if (x - i >= l) {
            segment.popBack();
          }
        }
      }
    }

    for (int i = 0; i < x; ++i) {
      for (int k = 0; k < z; ++k) {
        QueueWithMax<Point> segment = new QueueWithMax<Point>(w);
        for (int j = y - 1; j >= 0; --j) {
          segment.pushFront(maxs[i][j][k]);
          maxs[i][j][k] = segment.getMax();
          if (y - j >= w) {
            segment.popBack();
          }
        }
      }
    }

    StringTokenizer amountInputTokenizer = new StringTokenizer(buffer.readLine());
    Stack<Point> path = new Stack<Point>(x * y * z);
    int amount = Integer.parseInt(amountInputTokenizer.nextToken());
    for (int i = 0; i < amount; ++i) {
      StringTokenizer line = new StringTokenizer(buffer.readLine());
      Point start =
          ships[Integer.parseInt(line.nextToken())][Integer.parseInt(line.nextToken())][
              Integer.parseInt(line.nextToken())];
      Point max = start;
      do {
        start = max;
        path.pushFront(start);
        max = maxs[start.x][start.y][start.z];
      } while (!start.equals(max));

      while (!path.isEmpty()) {
        Point cur = path.popFront();
        maxs[cur.x][cur.y][cur.z] = max;
      }

      out.write(max + "\n");
    }
    out.flush();
  }

  static class Point implements Comparable<Point> {
    int x;
    int y;
    int z;
    long val;

    public Point(int x, int y, int z, long val) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.val = val;
    }

    @Override
    public int compareTo(Point o) {
      return Long.compare(val, o.val);
    }

    @Override
    public String toString() {
      return x + " " + y + " " + z;
    }
  }

  static class Stack<T> {
    private final Object[] stackArray;
    private int top;

    public Stack(int maxSize) {
      this.stackArray = new Object[maxSize];
      this.top = -1;
    }

    public T front() {
      return (T) stackArray[top];
    }

    public void pushFront(T element) {
      stackArray[++top] = element;
    }

    public T popFront() {
      return (T) stackArray[top--];
    }

    public boolean isEmpty() {
      return top < 0;
    }

    public int getSize() {
      return top + 1;
    }
  }

  static class Queue<T> {
    protected final Stack<T> left;
    protected final Stack<T> right;

    public Queue(int maxSize) {
      this.left = new Stack<T>(maxSize);
      this.right = new Stack<T>(maxSize);
    }

    private void moveStacksElements() {
      if (right.isEmpty()) {
        while (!left.isEmpty()) {
          right.pushFront(left.popFront());
        }
      }
    }

    public void pushFront(T element) {
      left.pushFront(element);
    }

    public T back() {
      moveStacksElements();
      return right.front();
    }

    public T popBack() {
      moveStacksElements();
      return right.popFront();
    }

    public boolean isEmpty() {
      return left.isEmpty() && right.isEmpty();
    }

    public int getSize() {
      return left.getSize() + right.getSize();
    }
  }

  static class QueueWithMax<T extends Comparable<T>> extends Queue<T> {
    private final Stack<T> leftMax;
    private final Stack<T> rightMax;

    public QueueWithMax(int maxSize) {
      super(maxSize);
      this.leftMax = new Stack<T>(maxSize);
      this.rightMax = new Stack<T>(maxSize);
    }

    private void moveStacksElements() {
      if (super.right.isEmpty()) {
        while (!super.left.isEmpty()) {
          if (super.right.isEmpty()) {
            rightMax.pushFront(super.left.front());
          } else {
            rightMax.pushFront(max(rightMax.front(), super.left.front()));
          }
          super.right.pushFront(super.left.popFront());
          leftMax.popFront();
        }
      }
    }

    @Override
    public void pushFront(T element) {
      moveStacksElements();
      super.left.pushFront(element);
      if (leftMax.isEmpty()) {
        leftMax.pushFront(element);
      } else {
        leftMax.pushFront(max(element, leftMax.front()));
      }
    }

    @Override
    public T popBack() {
      moveStacksElements();
      rightMax.popFront();
      return super.right.popFront();
    }

    @Override
    public T back() {
      moveStacksElements();
      return super.right.front();
    }

    public T getMax() {
      if (leftMax.isEmpty()) {
        return rightMax.front();
      } else if (rightMax.isEmpty()) {
        return leftMax.front();
      }
      return max(leftMax.front(), rightMax.front());
    }
  }
}
