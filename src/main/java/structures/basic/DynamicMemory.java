package structures.basic;

public class DynamicMemory<T> {
  protected Object[] array;
  protected Object[] previous;
  protected Object[] next;
  protected int size = 0;
  protected int nxt = 0;
  protected int prev = 0;

  public DynamicMemory() {
    this.array = new Object[8];
    this.previous = new Object[4];
    this.next = new Object[16];
  }

  protected int checkIndex(int index) {
    if (index < -size || index >= size){
      throw new IllegalArgumentException("Выход за границы массива");
    }
    if (index < 0) {
      return size - index;
    }
    return index;
  }

  protected void allocate() {
    this.previous = array;
    this.array = next;
    this.next = new Object[array.length << 1];
  }

  protected void deallocate() {
    this.next = array;
    this.array = previous;
    this.previous = new Object[array.length >> 1];
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

  public int getMaxSize() {
    return array.length;
  }

  public int getSize() {
    return size;
  }
}
