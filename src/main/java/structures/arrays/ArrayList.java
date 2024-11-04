package structures.arrays;

import structures.basic.DynamicMemory;

public class ArrayList<T> extends DynamicMemory<T> {
  public ArrayList() {
    super();
  }

  public void fromArray(T[] arr) {
    clear();
    for (var element : arr) {
      add(element);
    }
  }

  public void clear() {
    this.array = new Object[8];
    this.previous = new Object[4];
    this.next = new Object[16];
    this.size = 0;
    this.prev = 0;
    this.nxt = 0;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  // todo other funcs
}
