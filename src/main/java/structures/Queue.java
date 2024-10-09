package structures;

public class Queue<T> {
  protected final Stack<T> left;
  protected final Stack<T> right;

  public Queue(int maxSize) {
    this.left = new Stack<T>(maxSize);
    this.right = new Stack<T>(maxSize);
  }

  public Queue() {
    this.left = new Stack<T>();
    this.right = new Stack<T>();
  }

  private void moveStacksElements() {
    if (right.isEmpty()) {
      while (!left.isEmpty()) {
        right.pushFront(left.popFront());
      }
    }
  }

  public void pushFront(T element) {
    left.pushFront(element);
  }

  public T back() {
    moveStacksElements();
    return right.front();
  }

  public T popBack() {
    moveStacksElements();
    return right.popFront();
  }

  public boolean isEmpty() {
    return left.isEmpty() && right.isEmpty();
  }

  public int getSize() {
    return left.getSize() + right.getSize();
  }
}
