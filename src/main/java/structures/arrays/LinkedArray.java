package structures.arrays;

import java.util.NoSuchElementException;

public class LinkedArray<T> implements ArrayInterface<T>{
  private Node root;
  private Node leaf;
  private int size = 0;

  private class Node {
    private T value;
    private Node next;
    private Node previous;

    public Node(T value) {
      this.value = value;
    }

    public T getValue() {
      return this.value;
    }

    public Node next() {
      return this.next;
    }

    public Node previous() {
      return this.previous;
    }

    public void setValue(T value) {
      this.value = value;
    }

    public void setNext(Node next) {
      this.next = next;
    }

    public void setPrevious(Node previous) {
      this.previous = previous;
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
    Node thisNode = this.leaf;
    while (current != index) {
      current++;
      thisNode = thisNode.next();
    }
    return thisNode;
  }

  private Node goBack(int index) {
    index = checkIndex(index);
    int current = getSize() - 1;
    Node thisNode = this.root;
    while (current != index) {
      current--;
      thisNode = thisNode.previous();
    }
    return thisNode;
  }

  private Node getNearest(int index) {
    index = checkIndex(index);
    if (index > getSize() / 2) {
      return goBack(index);
    }
    return goForward(index);
  }

  private void connectNodes(Node left, Node right) {
    left.setNext(right);
    right.setNext(left);
  }

  @Override
  public T getAt(int index) {
    Node thisNode = getNearest(index);
    return thisNode.getValue();
  }

  @Override
  public T first() {
    return this.root.getValue();
  }

  @Override
  public T last() {
    return this.leaf.getValue();
  }

  @Override
  public void setAt(int index, T value) {
    Node thisNode = getNearest(index);
    thisNode.setValue(value);
  }

  @Override
  public int getIndex(T value) {
    int index = 0;
    Node thisNode = this.root;
    if (isEmpty()) {
      throw new NoSuchElementException("В массиве нет такого элемента");
    }

    if (this.leaf.getValue().equals(value)) {
      return getSize() - 1;
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
      T tmp = this.leaf.getValue();
      this.root = null;
      this.leaf = null;
      return tmp;
    }

    Node thisNode = this.leaf.previous();
    T tmp = this.leaf.getValue();
    thisNode.setNext(null);
    return tmp;
  }

  @Override
  public boolean remove(T value) {
    if (isEmpty()) {
      return false;
    }

    if (this.root.getValue().equals(value)) {
      if (getSize() == 1) {
        this.root = null;
        this.leaf = null;
        return true;
      }
      this.root = this.root.next();
      this.root.setPrevious(null);
      return true;
    }

    if (this.leaf.getValue().equals(value)) {
      this.leaf = this.leaf.previous();
      this.leaf.setNext(null);
      return true;
    }

    Node thisNode = this.root;

    while (!thisNode.getValue().equals(value)) {
      thisNode = thisNode.next();
      if (thisNode == null) {
        return false;
      }
    }

    thisNode.previous().setNext(thisNode.next());
    thisNode.next().setPrevious(thisNode.previous());

    size--;
    return true;
  }

  @Override
  public void remove(int index) {
    index = checkIndex(index);

    if (index == 0) {
      this.root = this.root.next();
      size--;

      if (isEmpty()) {
        clear();
      }
      return;
    } else if (index == getSize() - 1) {
      this.leaf = this.leaf.previous();
      this.leaf.setNext(null);
      return;
    }

    Node thisNode = getNearest(index);
    thisNode.previous().setNext(thisNode.next());
    thisNode.next().setPrevious(thisNode.previous());

    size--;
  }

  @Override
  public void insert(T value, int index) {
    Node newNode = new Node(value);

    if (index == 0) {
      connectNodes(newNode, this.root);
      this.root = newNode;
      size++;
      return;
    } else if (index == getSize()) {
      connectNodes(this.leaf, newNode);
      this.leaf = newNode;
    }

    Node thisNode = getNearest(index);
    connectNodes(thisNode.previous(), newNode);
    connectNodes(newNode, thisNode);

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
    this.leaf = null;
  }

  @Override
  public boolean isEmpty() {
    return this.size == 0;
  }
}
