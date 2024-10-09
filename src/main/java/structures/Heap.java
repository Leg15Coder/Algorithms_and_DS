package structures;

public class Heap<T extends Comparable<T>> {
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
