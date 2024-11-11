package structures.basic;

public class DynamicMemory<T> {
  protected Object[] array;
  protected Object[] previous;
  protected Object[] next;
  protected int size = 0;
  protected int nxt = 0;
  protected int prev = 0;

  public DynamicMemory() {
    this.array = new Object[2];
    this.previous = new Object[1];
    this.next = new Object[4];
  }

  protected int checkIndex(int index) {
    if (index < -size || index >= size){
      throw new IllegalArgumentException("Выход за границы массива");
    }
    if (index < 0) {
      return size + index;
    }
    return index;
  }

  protected void allocate() {
    this.previous = array;
    this.array = next;
    this.next = new Object[array.length << 1];
    this.nxt = 0;
  }

  protected void deallocate() { // todo later
    if (getMaxSize() == 2) {
      throw new RuntimeException("Нельзя деаллоцировать массив до размера 1");
    }
    this.next = array;
    this.array = previous;
    this.previous = new Object[array.length >> 1];
    this.prev = 0;
  }

  public void add(T value) {
    size++;
    if (size == array.length) {
      allocate();
    }
    array[size - 1] = value;
    if (size * 2 >= array.length) {
      this.next[nxt++] = array[2 * size - array.length];
      this.next[nxt++] = array[2 * size - array.length + 1];
    }
  }

  public T pop() {
    if (getSize() == 0) {
      throw new IllegalStateException("Нельзя удалять элементы из пустого объекта");
    }
    // if (size == array.length / 2) {
    //   deallocate();
    // }
    T result = (T) array[--size];
    if (nxt > 0) {
      nxt -= 2;
    }
    // if (prev + size + 1 == array.length) {
    //   System.out.print("*");
    //   this.previous[prev++] = array[array.length - size - 1];
    // }
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

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (var element : array) {
      result.append(element).append(" ");
    }
    return result.toString();
  }
}
