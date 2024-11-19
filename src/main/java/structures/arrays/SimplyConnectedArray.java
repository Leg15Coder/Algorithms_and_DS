package structures.arrays;

import java.util.NoSuchElementException;

public class SimplyConnectedArray<T> implements ArrayInterface<T>{
  private Node root;
  private int size = 0;

  private class Node {
    private T value;
    private Node next;

    public Node(T value) {
      this.value = value;
    }

    public T getValue() {
      return this.value;
    }

    public Node next() {
      return this.next;
    }

    public void setValue(T value) {
      this.value = value;
    }

    public void setNext(Node next) {
      this.next = next;
    }
  }

  private int checkIndex(int index) {
    if (index < -getSize() || index >= getSize()) {
      throw new IndexOutOfBoundsException("Выход за границы массива");
    }
    if (index < 0) {
      return getSize() + index;
    }
    return index;
  }

  private Node goForward(int index) {
    index = checkIndex(index);
    int current = 0;
    Node thisNode = this.root;
    while (current != index) {
      current++;
      thisNode = thisNode.next();
    }
    return thisNode;
  }

  @Override
  public T getAt(int index) {
    Node thisNode = goForward(index);
    return thisNode.getValue();
  }

  @Override
  public T first() {
    return this.root.getValue();
  }

  @Override
  public T last() {
    return getAt(getSize() - 1);
  }

  @Override
  public void setAt(int index, T value) {
    Node thisNode = goForward(index);
    thisNode.setValue(value);
  }

  @Override
  public int getIndex(T value) {
    int index = 0;
    Node thisNode = this.root;
    if (isEmpty()) {
      throw new NoSuchElementException("В массиве нет такого элемента");
    }

    while (!thisNode.getValue().equals(value)) {
      thisNode = thisNode.next();
      index++;
      if (thisNode == null) {
        throw new NoSuchElementException("В массиве нет такого элемента");
      }
    }
    return index;
  }

  @Override
  public T pop() {
    if (getSize() == 1) {
      T tmp = this.root.getValue();
      this.root = null;
      return tmp;
    }
    Node thisNode = goForward(getSize() - 2);
    T tmp = thisNode.next().getValue();
    thisNode.setNext(null);
    return tmp;
  }

  @Override
  public boolean remove(T value) {
    if (this.root.getValue().equals(value)) {
      this.root = this.root.next();
      return true;
    }

    if (isEmpty()) {
      return false;
    }

    int index = 0;
    Node thisNode = this.root;

    while (!thisNode.getValue().equals(value)) {
      thisNode = thisNode.next();
      index++;
      if (thisNode == null) {
        return false;
      }
    }
    remove(index);
    size--;
    return true;
  }

  @Override
  public void remove(int index) {
    index = checkIndex(index);

    if (index == 0) {
      this.root = this.root.next();
      size--;
      return;
    }

    Node thisNode = goForward(index - 1);
    thisNode.setNext(thisNode.next().next());
    size--;
  }

  @Override
  public void insert(T value, int index) {
    Node newNode = new Node(value);

    if (index == 0) {
      newNode.setNext(this.root);
      this.root = newNode;
      size++;
      return;
    }

    Node thisNode = goForward(index - 1);
    newNode.setNext(thisNode.next());
    thisNode.setNext(newNode);
    size++;
  }

  @Override
  public void insert(T value) {
    insert(value, getSize());
  }

  @Override
  public int getSize() {
    return this.size;
  }

  @Override
  public void clear() {
    this.size = 0;
    this.root = null;
  }

  @Override
  public boolean isEmpty() {
    return this.size == 0;
  }
}
