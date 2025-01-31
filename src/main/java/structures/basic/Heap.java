package structures.basic;

import structures.arrays.ArrayList;

public class Heap<T extends Comparable<T>> {
  private final DynamicMemory<T> HeapArray;
  private boolean isMin = true;
  private int size = 0;

  public Heap(boolean isMin) {
    this.HeapArray = new DynamicMemory<>();
    this.isMin = isMin;
  }

  public Heap() {
    this.HeapArray = new DynamicMemory<>();
  }

  public Heap(ArrayList<T> array, boolean isMin) {
    this.HeapArray = new DynamicMemory<>();
    this.size = array.getSize();
    this.isMin = isMin;
    heapify(array);
  }

  public Heap(ArrayList<T> array) {
    this.HeapArray = new DynamicMemory<>();
    this.size = array.getSize();
    heapify(array);
  }

  private void heapify(ArrayList<T> array) {
    for (var element : array) {
      this.HeapArray.add(element);
    }

    for (int i = array.getSize() / 2 - 1; i >= 0; --i) {
      siftDown(i);
    }
  }

  private boolean compare(T left, T right) {
    if (isMin) {
      return left.compareTo(right) < 0;
    } else {
      return left.compareTo(right) > 0;
    }
  }

  private void swap(int left, int right) {
    T tmp = (T) HeapArray.get(left);
    this.HeapArray.set(HeapArray.get(right), left);
    this.HeapArray.set(tmp, right);
  }

  private void siftUp(int index) {
    while (index > 0) {
      int parent = (index - 1) / 2;
      if (compare(HeapArray.get(index), HeapArray.get(parent))) {
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
          && compare(HeapArray.get(rightChild), HeapArray.get(leftChild))
          && compare(HeapArray.get(rightChild), HeapArray.get(index))) {
        swap(index, rightChild);
        index = rightChild;
      } else if (compare(HeapArray.get(leftChild), HeapArray.get(index))) {
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
    HeapArray.add(element);
    siftUp(size++);
  }

  public T root() {
    return HeapArray.get(0);
  }

  public T extract() {
    T result = root();
    this.HeapArray.set(HeapArray.get(--size), 0);
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
