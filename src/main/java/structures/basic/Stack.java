package structures.basic;

class Stack<T> {
  private final Object[] stackArray;
  private int top;

  public Stack(int maxSize) {
    this.stackArray = new Object[maxSize];
    this.top = -1;
  }

  public Stack() {
    this.stackArray = new Object[1 << 30];
    this.top = -1;
  }

  public T front() {
    return (T) stackArray[top];
  }

  public void pushFront(T element) {
    stackArray[++top] = element;
  }

  public T popFront() {
    return (T) stackArray[top--];
  }

  public boolean isEmpty() {
    return top < 0;
  }

  public int getSize() {
    return top + 1;
  }
}
