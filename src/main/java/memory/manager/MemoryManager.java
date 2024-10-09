package memory.manager;

import structures.Heap;

import static utils.Compare.min;

public class MemoryManager {
  private Heap<Allocation> memory;
  private final Allocation[] allocations;
  private int size;

  private static class Allocation implements Comparable<Allocation> {
    long start;
    long length;
    long end;

    Allocation(long start, long length) {
      this.start = start;
      this.length = length;
      this.end = start + length;
    }

    @Override
    public int compareTo(Allocation o) {
      if (this.length > o.length) {
        return 1;
      } else if (this.length == o.length && this.start < o.start) {
        return 1;
      }
      return -1;
    }
  }

  public MemoryManager(int MaxSize, int amount) {
    this.size = 0;
    this.memory = new Heap<Allocation>(amount + 1, false);
    this.memory.insert(new Allocation(1, MaxSize));
    this.allocations = new Allocation[amount];
  }

  public long allocate(long length) {
    if (memory.isEmpty() || memory.root().length < length) {
      return -1;
    }
    Allocation useful = memory.extract();
    Allocation TheUsed = new Allocation(useful.start, length);
    this.allocations[size++] = TheUsed;
    if (useful.length > length) {
      Allocation remains = new Allocation(useful.start + length, useful.length - length);
      memory.insert(remains);
    }
    return useful.start;
  }

  public void free(int index) {
    if (allocations[index - 1] != null) {
      MergeAllocs(allocations[index - 1]);
      allocations[index - 1] = null;
    }
    size++;
  }

  private void MergeAllocs(Allocation current) {
    Heap<Allocation> tmp = new Heap<Allocation>(this.size, false);
    while (!memory.isEmpty()) {
      Allocation ToCheck = memory.extract();
      if (ToCheck.end == current.start || ToCheck.start == current.end) {
        current = new Allocation(min(ToCheck.start, current.start), ToCheck.length + current.length);
      } else {
        tmp.insert(ToCheck);
      }
    }
    tmp.insert(current);
    this.memory = tmp;
  }
}
