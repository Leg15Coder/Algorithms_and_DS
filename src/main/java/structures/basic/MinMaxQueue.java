package structures.basic;

import static utils.Compare.max;
import static utils.Compare.min;

public class MinMaxQueue<T extends Comparable<T>> implements QueueInterface<T> {
  private class Node {
    public T min;
    public T val;
    public T max;

    public Node(T min, T val, T max) {
      this.min = min;
      this.val = val;
      this.max = max;
    }
  }

  protected final Stack<Node> pushStack;
  protected final Stack<Node> popStack;

  public MinMaxQueue() {
    this.pushStack = new Stack<>();
    this.popStack = new Stack<>();
  }

  private void moveStacksElements() {
    if (popStack.isEmpty()) {
      while (!pushStack.isEmpty()) {
        Node cur = pushStack.popFront();
        T element = cur.val;
        T mn = popStack.isEmpty() ? element : min(element, popStack.front().min);
        T mx = popStack.isEmpty() ? element : max(element, popStack.front().max);
        popStack.pushFront(new Node(mn, element, mx));
      }
    }
  }

  public void pushFront(T element) {
    T mn = pushStack.isEmpty() ? element : min(element, pushStack.front().min);
    T mx = pushStack.isEmpty() ? element : max(element, pushStack.front().max);
    pushStack.pushFront(new Node(mn, element, mx));
  }

  public T back() {
    if (isEmpty()) {
      return null;
    }
    moveStacksElements();
    return popStack.front().val;
  }

  public T popBack() {
    if (isEmpty()) {
      throw new IllegalStateException("Очередь пуста, невозможно удалить элемент");
    }
    moveStacksElements();
    return popStack.popFront().val;
  }

  public T getMin() {
    if (isEmpty()) {
      return null;
    }
    if (pushStack.isEmpty()) {
      return popStack.front().min;
    }
    if (popStack.isEmpty()) {
      return pushStack.front().min;
    }
    return min(pushStack.front().min, popStack.front().min);
  }

  public T getMax() {
    if (isEmpty()) {
      return null;
    }
    if (pushStack.isEmpty()) {
      return popStack.front().max;
    }
    if (popStack.isEmpty()) {
      return pushStack.front().max;
    }
    return max(pushStack.front().max, popStack.front().max);
  }

  public boolean isEmpty() {
    return pushStack.isEmpty() && popStack.isEmpty();
  }

  public int getSize() {
    return pushStack.getSize() + popStack.getSize();
  }
}
