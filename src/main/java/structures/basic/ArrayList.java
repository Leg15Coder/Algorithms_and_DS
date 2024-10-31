package structures.basic;

public class ArrayList<T> {
  private Object[] array;
  private Object[] previous;
  private Object[] next;
  int size = 0;
  int nxt = 0;
  int prev = 0;

  public ArrayList() {
    this.array = new Object[8];
    this.previous = new Object[4];
    this.next = new Object[16];
  }

  private int checkIndex(int index) {
    if (index < -size || index >= size){
      throw new IllegalArgumentException("Выход за границы массива");
    }
    if (index < 0) {
      return size - index;
    }
    return index;
  }

  private void allocate() {
    this.previous = array;
    this.array = next;
    this.next = new Object[array.length << 1];
  }

  private void deallocate() {
    this.next = array;
    this.array = previous;
    this.previous = new Object[array.length >> 1];
  }

  public T get(int index) {
    index = checkIndex(index);
    return (T) array[index];
  }

  public void set(T value, int index) {
    index = checkIndex(index);
    array[index] = value;
    next[index] = value;
    if (index < array.length / 2) {
      previous[index] = value;
    }
  }

  public void add(T value) {
    size++;
    if (size == array.length) {
      allocate();
    }
    array[size] = value;
    next[nxt++] = array[2 * size - array.length];
    next[nxt++] = array[2 * size - array.length + 1];
  }

  public T pop() {
    if (size == array.length / 2 - 1) {
      deallocate();
    }
    T result = (T) array[--size];
    previous[prev++] = array[array.length - size];
    return result;
  }

  public void fromArray(T[] arr) {
    clear();
    for (var element : arr) {
      add(element);
    }
  }

  public void clear() {
    this.array = new Object[8];
    this.previous = new Object[4];
    this.next = new Object[16];
    this.size = 0;
    this.prev = 0;
    this.nxt = 0;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int getMaxSize() {
    return array.length;
  }

  public int getSize() {
    return size;
  }
}
