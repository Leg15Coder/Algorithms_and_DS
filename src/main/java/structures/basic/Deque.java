package structures.basic;

public class Deque<T> extends Queue<T> {
  public Deque() {
    super();
  }

  private void moveStacksElementsLeft() {
    if (popStack.isEmpty()) {
      while (!pushStack.isEmpty()) {
        popStack.pushFront(pushStack.popFront());
      }
    }
  }

  private void moveStacksElementsRight() {
    if (pushStack.isEmpty()) {
      while (!popStack.isEmpty()) {
        pushStack.pushFront(popStack.popFront());
      }
    }
  }

  public void pushBack(T element) {
    popStack.pushFront(element);
  }

  @Override
  public T back() {
    if (isEmpty()) {
      return null;
    }
    moveStacksElementsLeft();
    return popStack.front();
  }

  public T front() {
    if (isEmpty()) {
      return null;
    }
    moveStacksElementsRight();
    return pushStack.front();
  }

  @Override
  public T popBack() {
    if (isEmpty()) {
      throw new IllegalStateException("Дек пуст, невозможно удалить элемент");
    }
    moveStacksElementsLeft();
    return popStack.popFront();
  }

  public T popFront() {
    if (isEmpty()) {
      throw new IllegalStateException("Дек пуст, невозможно удалить элемент");
    }
    moveStacksElementsRight();
    return pushStack.popFront();
  }
}
