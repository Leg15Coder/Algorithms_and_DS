package structures.trees.segmentation;

import structures.basic.ArrayList;

public class SegmentTree<T> {
  private class Node {
    T value;

    public Node(T value) {
      this.value = value;
    }
  }

  private final ArrayList<Node> array = new ArrayList<>();
  private final Operation<T> operation;
  private int size;

  public SegmentTree(T[] array, Operation<T> operation) {
    this.array.fromArray((Node[]) new Object[array.length << 2]);
    this.operation = operation;
    this.size = this.array.getSize();
    build(array, 0, new Range(0, size));
  }

  private void update(int cur) {
    Node newNode = new Node(operation.func(this.array.get(2 * cur + 1).value, this.array.get(2 * cur + 2).value));
    this.array.set(newNode, cur);
  }

  private void build(T[] array, int cur, Range seg) {
    if (seg.length() == 1) {
      this.array.set(new Node(array[seg.getLeft()]), cur);
      return;
    }
    build(array, 2 * cur + 1, new Range(seg.getLeft(), seg.middle()));
    build(array, 2 * cur + 2, new Range(seg.middle(), seg.getRight()));
    update(cur);
  }

  public T get(int left, int right) {
    return get(0, new Range(0, size), new Range(left, right + 1));
  }

  public T get(Range seg) {
    return get(0, new Range(0, size), seg);
  }

  public T get(int index) {
    return get(0, new Range(0, size), new Range(index, index + 1));
  }

  private T get(int cur, Range curSeg, Range absSeg) {
    if (absSeg.isInside(curSeg)) {
      return this.array.get(cur).value;
    }
    if (absSeg.isOutside(curSeg)) {
      return operation.neutral();
    }
    Range leftRange = new Range(curSeg.getLeft(), curSeg.middle());
    Range rightRange = new Range(curSeg.middle(), curSeg.getRight());
    return operation.func(get(2 * cur + 1, leftRange, absSeg), get(2 * cur + 2, rightRange, absSeg));
  }

  public void set(T value, int index) {
    set(value, 0, index, new Range(0, size));
  }

  private void set(T value, int cur, int index, Range seg) {
    if (seg.length() == 1) {
      this.array.set(new Node(value), cur);
      return;
    }
    if (seg.where(index) != 0) {
      return;
    }
    set(value, 2 * cur + 1, index, new Range(seg.getLeft(), seg.middle()));
    set(value, 2 * cur + 2, index, new Range(seg.middle(), seg.getRight()));
    update(cur);
  }
}
