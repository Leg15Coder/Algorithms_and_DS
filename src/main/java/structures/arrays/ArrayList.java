package structures.arrays;

import structures.basic.DynamicMemory;
import structures.basic.exceptions.DynamicMemoryException;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayList<T> implements ArrayInterface<T> {
  private DynamicMemory<T> memory;

  public ArrayList() {
    this.memory = new DynamicMemory<>();
  }

  public ArrayList(Iterable<T> values) {
    this.memory = new DynamicMemory<>();
    for (var value : values) {
      insert(value);
    }
  }

  @Override
  public void addAllFromList(Iterable<T> values) {
    for (var value : values) {
      insert(value);
    }
  }

  @Override
  public T getAt(int index) {
    try {
      return memory.get(index);
    } catch (DynamicMemoryException e) {
      throw new IndexOutOfBoundsException("Выход за границы массива");
    }
  }

  @Override
  public T first() {
    return getAt(0);
  }

  @Override
  public T last() {
    return getAt(-1);
  }

  @Override
  public void setAt(int index, T value) {
    try {
      memory.set(value, index);
    } catch (DynamicMemoryException e) {
      throw new IndexOutOfBoundsException("Выход за границы массива");
    }
  }

  @Override
  public int getIndex(T value) {
    for (int index = 0; index < getSize(); ++index) {
      if (getAt(index).equals(value)) {
        return index;
      }
    }
    throw new NoSuchElementException("В массиве нет такого элемента");
  }

  @Override
  public T pop() {
    try {
      return memory.pop();
    } catch (DynamicMemoryException e) {
      throw new IndexOutOfBoundsException("Нельзя удалить элемент из пустого массива");
    }
  }

  @Override
  public boolean remove(T value) {
    try {
      int index = getIndex(value);
      remove(index);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public void remove(int index) {
    try {
      getAt(index);

      if (index < 0) {
        index = -index - 1;
      }
    } catch (DynamicMemoryException e) {
      throw new IndexOutOfBoundsException("Выход за границы массива");
    }

    while (index < getSize()) {
      ++index;

      if (index < getSize()) {
        memory.set(getAt(index), index - 1);
      }
    }
    memory.pop();
  }

  @Override
  public void insert(T value, int index) {
    if (index == getSize() || index == -(getSize() + 1)) {
      insert(value);
      return;
    }

    try {
      getAt(index);

      if (index < 0) {
        index = -index - 1;
      }
    } catch (DynamicMemoryException e) {
      throw new IndexOutOfBoundsException("Выход за границы массива");
    }

    T tmp = value;
    while (index <= getSize()) {
      if (index == getSize()) {
        memory.add(tmp);
        break;
      }

      T next = memory.get(index);
      memory.set(tmp, index++);
      tmp = next;
    }
  }

  @Override
  public void insert(T value) {
    memory.add(value);
  }

  @Override
  public int getSize() {
    return memory.getSize();
  }

  @Override
  public void clear() {
    this.memory = new DynamicMemory<>();
  }

  @Override
  public boolean isEmpty() {
    return getSize() == 0;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder("[");
    for (int index = 0; index < getSize(); ++index) {
      result.append(getAt(index));

      if (index + 1 != getSize()) {
        result.append(", ");
      }
    }
    result.append("]");
    return result.toString();
  }

  @Override
  public Iterator<T> iterator() {
    return new Iterator<>() {
      private int currentIndex = 0;

      @Override
      public boolean hasNext() {
        return currentIndex < getSize();
      }

      @Override
      public T next() {
        if (!hasNext()) {
          throw new NoSuchElementException("Больше нет элементов для итерации");
        }
        return getAt(currentIndex++);
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("Удаление элементов через итератор не поддерживается");
      }
    };
  }
}
