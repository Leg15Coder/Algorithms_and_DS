package structures.basic;

public class Stack<T> {
  private final DynamicMemory<T> stackArray = new DynamicMemory<>();

  public T front() {
    if (isEmpty()) {
      return null;
    }
    return stackArray.get(stackArray.getSize() - 1);
  }

  public void pushFront(T element) {
    stackArray.add(element);
  }

  public T popFront() {
    if (isEmpty()) {
      throw new IllegalStateException("Стек пуст, невозможно удалить элемент");
    }
    return stackArray.pop();
  }

  public boolean isEmpty() {
    return stackArray.getSize() == 0;
  }

  public int getSize() {
    return stackArray.getSize();
  }
}
