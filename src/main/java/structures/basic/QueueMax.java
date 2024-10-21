package structures.basic;

import structures.basic.Stack;

import static utils.Compare.max;

public class QueueMax {
  static class QueueWithMax<T extends Comparable<T>> extends Queue<T> {
    private final Stack<T> leftMax;
    private final Stack<T> rightMax;

    public QueueWithMax(int maxSize) {
      super(maxSize);
      this.leftMax = new Stack<T>(maxSize);
      this.rightMax = new Stack<T>(maxSize);
    }

    public QueueWithMax() {
      super();
      this.leftMax = new Stack<T>();
      this.rightMax = new Stack<T>();
    }

    private void moveStacksElements() {
      if (super.right.isEmpty()) {
        while (!super.left.isEmpty()) {
          if (super.right.isEmpty()) {
            rightMax.pushFront(super.left.front());
          } else {
            rightMax.pushFront(max(rightMax.front(), super.left.front()));
          }
          super.right.pushFront(super.left.popFront());
          leftMax.popFront();
        }
      }
    }

    @Override
    public void pushFront(T element) {
      moveStacksElements();
      super.left.pushFront(element);
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
      return super.right.popFront();
    }

    @Override
    public T back() {
      moveStacksElements();
      return super.right.front();
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
