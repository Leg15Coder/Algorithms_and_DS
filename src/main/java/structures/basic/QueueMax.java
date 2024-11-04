package structures.basic;

import static utils.Compare.max;

public class QueueMax {
  static class QueueWithMax<T extends Comparable<T>> extends Queue<T> {
    private final Stack<T> leftMax;
    private final Stack<T> rightMax;

    public QueueWithMax() {
      super();
      this.leftMax = new Stack<T>();
      this.rightMax = new Stack<T>();
    }

    private void moveStacksElements() {
      if (super.popStack.isEmpty()) {
        while (!super.pushStack.isEmpty()) {
          if (super.popStack.isEmpty()) {
            rightMax.pushFront(super.pushStack.front());
          } else {
            rightMax.pushFront(max(rightMax.front(), super.pushStack.front()));
          }
          super.popStack.pushFront(super.pushStack.popFront());
          leftMax.popFront();
        }
      }
    }

    @Override
    public void pushFront(T element) {
      moveStacksElements();
      super.pushStack.pushFront(element);
      if (leftMax.isEmpty()) {
        leftMax.pushFront(element);
      } else {
        leftMax.pushFront(max(element, leftMax.front()));
      }
    }

    @Override
    public T popBack() {
      moveStacksElements();
      rightMax.popFront();
      return super.popStack.popFront();
    }

    @Override
    public T back() {
      moveStacksElements();
      return super.popStack.front();
    }

    public T getMax() {
      if (leftMax.isEmpty()) {
        return rightMax.front();
      } else if (rightMax.isEmpty()) {
        return leftMax.front();
      }
      return max(leftMax.front(), rightMax.front());
    }
  }
}
