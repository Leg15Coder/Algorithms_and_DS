import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    int amount = input.nextInt();
    MinMaxHeap<Long> tree = new MinMaxHeap<Long>(200000);
    for (int i = 0; i < amount; ++i) {
      String command = input.next();
      switch (command) {
        case ("insert"):
          long value = input.nextLong();
          tree.insert(value);
          System.out.println("ok");
          break;
        case ("extract_min"):
          if (tree.isEmpty()) {
            System.out.println("error");
            break;
          }
          System.out.println(tree.extract_min());
          break;
        case ("get_min"):
          if (tree.isEmpty()) {
            System.out.println("error");
            break;
          }
          System.out.println(tree.get_min());
          break;
        case ("extract_max"):
          if (tree.isEmpty()) {
            System.out.println("error");
            break;
          }
          System.out.println(tree.extract_max());
          break;
        case ("get_max"):
          if (tree.isEmpty()) {
            System.out.println("error");
            break;
          }
          System.out.println(tree.get_max());
          break;
        case ("size"):
          System.out.println(tree.size());
          break;
        default:
          tree.clear();
          System.out.println("ok");
          break;
      }
    }
  }

  public static class Heap<T extends Comparable<T>> {
    private final Object[] HeapArray;
    private boolean IsMin = true;
    private int size = 0;

    public Heap(int maxSize, boolean IsMin) {
      this.HeapArray = new Object[maxSize];
      this.IsMin = IsMin;
    }

    public Heap(int maxSize) {
      this.HeapArray = new Object[maxSize];
    }

    public Heap(boolean IsMin) {
      this.HeapArray = new Object[1 << 27];
      this.IsMin = IsMin;
    }

    public Heap() {
      this.HeapArray = new Object[1 << 27];
    }

    private boolean compare(T left, T right) {
      if (IsMin) {
        return left.compareTo(right) < 0;
      } else {
        return left.compareTo(right) > 0;
      }
    }

    private void swap(int left, int right) {
      T tmp = (T) HeapArray[left];
      this.HeapArray[left] = HeapArray[right];
      this.HeapArray[right] = tmp;
    }

    private void siftUp(int index) {
      while (index > 0) {
        int parent = (index - 1) / 2;
        if (compare((T) HeapArray[index], (T) HeapArray[parent])) {
          swap(index, parent);
          index = parent;
        } else {
          break;
        }
      }
    }

    private void siftDown(int index) {
      int leftChild = 2 * index + 1;
      int rightChild = 2 * index + 2;
      while (leftChild < size) {
        if ((rightChild < size)
            && compare((T) HeapArray[rightChild], (T) HeapArray[leftChild])
            && compare((T) HeapArray[rightChild], (T) HeapArray[index])) {
          swap(index, rightChild);
          index = rightChild;
        } else if (compare((T) HeapArray[leftChild], (T) HeapArray[index])) {
          swap(index, leftChild);
          index = leftChild;
        } else {
          break;
        }
        leftChild = 2 * index + 1;
        rightChild = 2 * index + 2;
      }
    }

    public void insert(T element) {
      HeapArray[size] = element;
      siftUp(size++);
    }

    public T root() {
      return (T) HeapArray[0];
    }

    public T extract() {
      T result = root();
      this.HeapArray[0] = HeapArray[--size];
      if (!isEmpty()) {
        siftDown(0);
      }
      return result;
    }

    public int size() {
      return size;
    }

    public void clear() {
      size = 0;
    }

    public boolean isEmpty() {
      return size == 0;
    }
  }

  public static class MinMaxHeap<T extends Comparable<T>> {
    private final Heap<T> MinHeap;
    private final Heap<T> MaxHeap;
    private final Heap<T> DeletedMin;
    private final Heap<T> DeletedMax;
    private int size = 0;

    public MinMaxHeap(int maxSize) {
      this.MaxHeap = new Heap<T>(maxSize, false);
      this.MinHeap = new Heap<T>(maxSize, true);
      this.DeletedMin = new Heap<T>(maxSize, true);
      this.DeletedMax = new Heap<T>(maxSize, false);
    }

    public MinMaxHeap() {
      this.MaxHeap = new Heap<T>(false);
      this.MinHeap = new Heap<T>(true);
      this.DeletedMin = new Heap<T>(true);
      this.DeletedMax = new Heap<T>(false);
    }

    public void insert(T element) {
      isEmpty();
      MinHeap.insert(element);
      MaxHeap.insert(element);
      size++;
    }

    public T extract_min() {
      T min = MinHeap.extract();
      if (!DeletedMin.isEmpty() && DeletedMin.root().equals(min)) {
        DeletedMin.extract();
        return extract_min();
      }
      size--;
      this.DeletedMax.insert(min);
      return min;
    }

    public T extract_max() {
      T max = MaxHeap.extract();
      if (!DeletedMax.isEmpty() && DeletedMax.root().equals(max)) {
        DeletedMax.extract();
        return extract_max();
      }
      size--;
      this.DeletedMin.insert(max);
      return max;
    }

    public T get_min() {
      T min = MinHeap.root();
      if (!DeletedMin.isEmpty() && DeletedMin.root().equals(min)) {
        DeletedMin.extract();
        MinHeap.extract();
        return get_min();
      }
      return min;
    }

    public T get_max() {
      T max = MaxHeap.root();
      if (!DeletedMax.isEmpty() && DeletedMax.root().equals(max)) {
        DeletedMax.extract();
        MaxHeap.extract();
        return get_max();
      }
      return max;
    }

    public int size() {
      return size;
    }

    public void clear() {
      this.size = 0;
      MaxHeap.clear();
      MinHeap.clear();
      DeletedMin.clear();
      DeletedMax.clear();
    }

    public boolean isEmpty() {
      if (size == 0) {
        clear();
        return true;
      }
      return false;
    }
  }
}
