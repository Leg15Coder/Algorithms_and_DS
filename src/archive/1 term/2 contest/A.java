import java.util.Scanner;

public class Main {
  public static <T extends Comparable<T>> T max(T a, T b) {
    return (a.compareTo(b) > 0) ? a : b;
  }

  public static <T extends Comparable<T>> T min(T a, T b) {
    return (a.compareTo(b) > 0) ? b : a;
  }

  public static class DynamicMemory<T> {
    protected Object[] array;
    protected Object[] previous;
    protected Object[] next;
    protected int size = 0;
    protected int indexInNext = 0;
    protected int indexInPrevious = 0;

    public DynamicMemory() {
      this.array = new Object[2];
      this.previous = new Object[1];
      this.next = new Object[4];
    }

    protected int checkIndex(int index) {
      if (index < -size || index >= size){
        throw new IllegalArgumentException("Выход за границы массива");
      }
      if (index < 0) {
        return size + index;
      }
      return index;
    }

    protected void allocate() {
      this.previous = array;
      this.array = next;
      this.next = new Object[array.length << 1];
      this.indexInNext = 0;
    }

    protected void deallocate() { // todo update pop() with deallocate()
      if (getMaxSize() == 2) {
        throw new RuntimeException("Нельзя деаллоцировать массив до размера 1");
      }
      this.next = array;
      this.array = previous;
      this.previous = new Object[array.length >> 1];
      this.indexInPrevious = 0;
    }

    public void add(T value) {
      size++;
      if (size == array.length) {
        allocate();
      }
      array[size - 1] = value;
      if (size * 2 >= array.length) {
        this.next[indexInNext++] = array[2 * size - array.length];
        this.next[indexInNext++] = array[2 * size - array.length + 1];
      }
    }

    public T pop() {
      if (getSize() == 0) {
        throw new IllegalStateException("Нельзя удалять элементы из пустого объекта");
      }
      T result = (T) array[--size];
      if (indexInNext > 0) {
        indexInNext -= 2;
      }
      return result;
    }

    public T get(int index) {
      index = checkIndex(index);
      return (T) array[index];
    }

    public void set(T value, int index) {
      index = checkIndex(index);
      array[index] = value;
      next[index] = value;
      if (index < array.length / 2) {
        previous[index] = value;
      }
    }

    public int getMaxSize() {
      return array.length;
    }

    public int getSize() {
      return size;
    }

    @Override
    public String toString() {
      StringBuilder result = new StringBuilder();
      for (var element : array) {
        result.append(element).append(" ");
      }
      return result.toString();
    }
  }

  public static class Stack<T> {
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

  public static class Queue<T> implements QueueInterface<T> {
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

  public static class MinMaxQueue<T extends Comparable<T>> implements QueueInterface<T> {
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

  public interface QueueInterface<T> {
    T back();

    void pushFront(T element);

    T popBack();

    int getSize();

    boolean isEmpty();
  }

  public static class CommandManager {
    int current = 0;

    public void execute(MinMaxQueue<Long> queue, long[] array, char command) {
      if (command == 'R') {
        queue.pushFront(array[++current]);
        System.out.print(queue.getMax() + " ");
      } else {
        queue.popBack();
        System.out.print(queue.getMax() + " ");
      }
    }
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    int amount = input.nextInt();
    long[] array = new long[amount];
    for (int i = 0; i < array.length; i++) {
      array[i] = input.nextLong();
    }

    int requests = input.nextInt();

    MinMaxQueue<Long> queue = new MinMaxQueue<>();
    queue.pushFront(array[0]);
    CommandManager commandManager = new CommandManager();

    for (int i = 0; i < requests; i++) {
      char symb = input.next().charAt(0);
      commandManager.execute(queue, array, symb);
    }
  }
}
