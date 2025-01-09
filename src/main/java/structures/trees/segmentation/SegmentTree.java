package structures.trees.segmentation;

import structures.basic.ArrayList;

public class SegmentTree<T> {
  private class Node {
    private T value;
    private T push = updateOperation.neutral();
    private boolean hasPushedKids = false;

    public Node(T value) {
      this.value = value;
    }

    private void clear() {
      this.hasPushedKids = true;
      this.value = coreOperation.neutral();
      this.push = null;
    }

    public void setToPush(T value) {
      if (value == null) {
        clear();
        return;
      }

      this.hasPushedKids = true;
      this.push = updateOperation.func(push, value);
    }

    public T getToPush() {
      this.hasPushedKids = false;
      T result = push;
      this.push = updateOperation.neutral();
      return result;
    }

    public void apply() {
      this.value = updateOperation.func(value, getToPush());
    }

    public boolean checkKids() {
      return hasPushedKids;
    }

    public void checkKids(int cur) {
      this.hasPushedKids = array.get(2 * cur + 1).checkKids() || array.get(2 * cur + 1).checkKids();
    }

    public void update(int cur) {
      this.value = updateOperation.func(
          coreOperation.func(array.get(2 * cur + 1).value, array.get(2 * cur + 2).value),
          this.push
        );
      checkKids(cur);
    }
  }

  private final ArrayList<Node> array = new ArrayList<>();
  private final Operation<T> coreOperation;
  private final Operation<T> updateOperation;
  private final int size;

  public SegmentTree(T[] array, Operation<T> coreOperation, Operation<T> updateOperation) {
    this.array.fromArray((Node[]) new Object[array.length << 2]);
    this.updateOperation = updateOperation;
    this.coreOperation = coreOperation;
    this.size = this.array.getSize();
    build(array, 0, new Range(0, size));
  }

  private void update(int cur) {
    this.array.get(cur).update(cur);
  }

  private void push(int cur, Range seg) {
    if (!this.array.get(cur).checkKids()) {
      return;
    }

    if (seg.length() == 1) {
      this.array.get(cur).apply();
      return;
    }

    T toPush = this.array.get(cur).getToPush();
    this.array.get(2 * cur + 1).setToPush(toPush);
    this.array.get(2 * cur + 2).setToPush(toPush);
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
    push(cur, curSeg);

    if (absSeg.isInside(curSeg)) {
      return this.array.get(cur).value;
    }
    if (absSeg.isOutside(curSeg)) {
      return coreOperation.neutral();
    }

    Range leftRange = new Range(curSeg.getLeft(), curSeg.middle());
    Range rightRange = new Range(curSeg.middle(), curSeg.getRight());
    return coreOperation.func(get(2 * cur + 1, leftRange, absSeg), get(2 * cur + 2, rightRange, absSeg));
  }

  public void set(T value, int index) {
    set(value, 0, index, new Range(0, size));
  }

  private void set(T value, int cur, int index, Range seg) {
    push(cur, seg);

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

  public void updateSegmentWith(T value, Range seg) {
    updateSegmentWith(value, 0, new Range(0, size), seg);
  }

  public void updateSegmentWith(T value, int left, int right) {
    updateSegmentWith(value, 0, new Range(0, size), new Range(left, right + 1));
  }

  private void updateSegmentWith(T value, int cur, Range curSeg, Range absSeg) {
    push(cur, curSeg);

    if (absSeg.isInside(curSeg)) {
      this.array.get(cur).setToPush(value);
      return;
    }
    if (absSeg.isOutside(curSeg)) {
      return;
    }

    Range leftRange = new Range(curSeg.getLeft(), curSeg.middle());
    Range rightRange = new Range(curSeg.middle(), curSeg.getRight());

    updateSegmentWith(value, 2 * cur + 1, leftRange, absSeg);
    updateSegmentWith(value, 2 * cur + 2, rightRange, absSeg);
    update(cur);
  }

  public void setOnSegment(T value, int left, int right) {
    setOnSegment(value, new Range(left, right + 1));
  }

  public void setOnSegment(T value, Range seg) {
    clearSegment(seg);
    updateSegmentWith(value, seg);
  }

  public void clearSegment(int left, int right) {
    clear(0, new Range(0, size), new Range(left, right + 1));
  }

  public void clearSegment(Range seg) {
    clear(0, new Range(0, size), seg);
  }

  public void clear(int index) {
    clear(0, new Range(0, size), new Range(index, index + 1));
  }

  private void clear(int cur, Range curSeg, Range absSeg) {
    push(cur, absSeg);

    if (absSeg.isInside(curSeg)) {
      this.array.get(cur).setToPush(null);
      return;
    }
    if (absSeg.isOutside(curSeg)) {
      return;
    }

    Range leftRange = new Range(curSeg.getLeft(), curSeg.middle());
    Range rightRange = new Range(curSeg.middle(), curSeg.getRight());

    clear(2 * cur + 1, leftRange, absSeg);
    clear(2 * cur + 2, rightRange, absSeg);
    update(cur);
  }

  public int getSize() {
    return size;
  }
}
