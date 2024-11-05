package structures.basic;

public class MinMaxHeap<T extends Comparable<T>> {
  private final Heap<T> MinHeap;
  private final Heap<T> MaxHeap;
  private final Heap<T> DeletedMin;
  private final Heap<T> DeletedMax;
  private int size = 0;

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
