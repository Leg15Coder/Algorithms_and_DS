package structures.basic;

public class Queue<T> implements QueueInterface<T> {
  protected final Stack<T> pushStack;
  protected final Stack<T> popStack;

  public Queue() {
    this.pushStack = new Stack<>();
    this.popStack = new Stack<>();
  }

  private void moveStacksElements() {
    if (popStack.isEmpty()) {
      while (!pushStack.isEmpty()) {
        popStack.pushFront(pushStack.popFront());
      }
    }
  }

  public void pushFront(T element) {
    pushStack.pushFront(element);
  }

  public T back() {
    if (isEmpty()) {
      return null;
    }
    moveStacksElements();
    return popStack.front();
  }

  public T popBack() {
    if (isEmpty()) {
      throw new IllegalStateException("Очередь пуста, невозможно удалить элемент");
    }
    moveStacksElements();
    return popStack.popFront();
  }

  public boolean isEmpty() {
    return pushStack.isEmpty() && popStack.isEmpty();
  }

  public int getSize() {
    return pushStack.getSize() + popStack.getSize();
  }
}
