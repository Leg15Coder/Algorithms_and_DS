import java.util.Scanner;

public class Main {
  public static <T extends Comparable<T>> T max(T a, T b) {
    return (a.compareTo(b) > 0) ? a : b;
  }

  static class Stack<T> {
    private final Object[] stackArray;
    private int top;

    public Stack(int maxSize) {
      this.stackArray = new Object[maxSize];
      this.top = -1;
    }

    public Stack() {
      this.stackArray = new Object[1 << 30];
      this.top = -1;
    }

    @SuppressWarnings("unchecked")
    public T front() {
      return (T) stackArray[top];
    }

    public void pushFront(T element) {
      stackArray[++top] = element;
    }

    @SuppressWarnings("unchecked")
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
    private final Stack<T> left;
    private final Stack<T> right;

    public Queue(int maxSize) {
      this.left = new Stack<T>(maxSize);
      this.right = new Stack<T>(maxSize);
    }

    public Queue() {
      this.left = new Stack<T>();
      this.right = new Stack<T>();
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

    public QueueWithMax() {
      super();
      this.leftMax = new Stack<T>();
      this.rightMax = new Stack<T>();
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

  static class Sortings {
    public static long MergeSort(long[] array) {
      return MergeSortWithCount(array, 0, array.length - 1);
    }

    private static long MergeSortWithCount(long[] array, int left, int right) {
      long count = 0;
      if (left < right) {
        int middle = (right + left) / 2;
        count += MergeSortWithCount(array, left, middle);
        count += MergeSortWithCount(array, middle + 1, right);
        count += merge(array, left, middle, right);
      }
      return count;
    }

    private static long merge(long[] array, int left, int middle, int right) {
      long[] leftArray = new long[middle - left + 1];
      long[] rightArray = new long[right - middle];

      System.arraycopy(array, left, leftArray, 0, leftArray.length);
      System.arraycopy(array, middle + 1, rightArray, 0, rightArray.length);

      int i = 0, j = 0, k = left;
      long count = 0;

      while (i < leftArray.length && j < rightArray.length) {
        if (leftArray[i] <= rightArray[j]) {
          array[k++] = leftArray[i++];
        } else {
          array[k++] = rightArray[j++];
          count += middle + 1L - left - i;
        }
      }
      while (i < leftArray.length) {
        array[k++] = leftArray[i++];
      }
      while (j < rightArray.length) {
        array[k++] = rightArray[j++];
      }
      return count;
    }
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    int n = input.nextInt();
    long[] array = new long[n];
    for (int i = 0; i < array.length; i++) {
      array[i] = input.nextLong();
    }
    System.out.println(Sortings.MergeSort(array));
  }
}
