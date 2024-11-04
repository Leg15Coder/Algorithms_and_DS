package structures.basic;

class Stack<T> {
  private final DynamicMemory<T> stackArray = new DynamicMemory<>();

  public T front() {
    return stackArray.get(stackArray.getSize() - 1);
  }

  public void pushFront(T element) {
    stackArray.add(element);
  }

  public T popFront() {
    return stackArray.pop();
  }

  public boolean isEmpty() {
    return stackArray.getSize() == 0;
  }

  public int getSize() {
    return stackArray.getSize();
  }
}
