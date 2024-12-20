import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws Exception {
    BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer BaseInputTokenizer = new StringTokenizer(buffer.readLine());
    int MaxLength = Integer.parseInt(BaseInputTokenizer.nextToken());
    int amount = Integer.parseInt(BaseInputTokenizer.nextToken());
    BufferedWriter log = new BufferedWriter(new OutputStreamWriter(System.out));
    MemoryManager memoryManager = new MemoryManager(MaxLength, amount);
    for (int i = 1; i <= amount; i++) {
      StringTokenizer tokenizer = new StringTokenizer(buffer.readLine());
      int request = Integer.parseInt(tokenizer.nextToken());
      if (request > 0) {
        long result = memoryManager.allocate(request);
        log.write(result + "\n");
      } else {
        memoryManager.free(-request);
      }
    }
    log.flush();
  }

  static class Allocation implements Comparable<Allocation> {
    long start;
    long length;

    Allocation(long start, long length) {
      this.start = start;
      this.length = length;
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

  public static class MemoryManager {
    private Heap memory;
    private final Allocation[] allocations;
    private int size;

    public MemoryManager(int MaxSize, int amount) {
      this.size = 0;
      this.memory = new Heap(amount + 1, false);
      this.memory.insert(new Allocation(1, MaxSize));
      this.allocations = new Allocation[amount];
    }

    public long allocate(long length) {
      if (memory.isEmpty() || memory.root().length < length) {
        size++;
        return -1;
      }
      Allocation useful = memory.extract();
      Allocation TheUsed = new Allocation(useful.start, length);
      this.allocations[size] = TheUsed;
      if (useful.length > length) {
        Allocation remains = new Allocation(useful.start + length, useful.length - length);
        memory.insert(remains);
      }
      size++;
      return useful.start;
    }

    public void free(int index) {
      Allocation current = allocations[index - 1];
      if (current != null) {
        Allocation rightAlloc = memory.findByStart(current.start + current.length);
        Allocation leftAlloc = memory.findByEnd(current.start);
        if (rightAlloc != null && leftAlloc != null) {
          current.start = leftAlloc.start;
          current.length += leftAlloc.length + rightAlloc.length;
        } else if (rightAlloc != null) {
          current.length += rightAlloc.length;
        } else if (leftAlloc != null) {
          current.start = leftAlloc.start;
          current.length += leftAlloc.length;
        }
        memory.insert(current);
        allocations[index - 1] = null;
      }
      size++;
    }
  }

  public static class Heap {
    private final Allocation[] HeapArray;
    private boolean IsMin = true;
    private int size = 0;

    public Heap(int maxSize, boolean IsMin) {
      this.HeapArray = new Allocation[maxSize];
      this.IsMin = IsMin;
    }

    private boolean compare(Allocation left, Allocation right) {
      if (IsMin) {
        return left.compareTo(right) < 0;
      } else {
        return left.compareTo(right) > 0;
      }
    }

    private void swap(int left, int right) {
      Allocation tmp = HeapArray[left];
      this.HeapArray[left] = HeapArray[right];
      this.HeapArray[right] = tmp;
    }

    private void siftUp(int index) {
      while (index > 0) {
        int parent = (index - 1) / 2;
        if (compare(HeapArray[index], HeapArray[parent])) {
          swap(index, parent);
          index = parent;
        } else {
          break;
        }
      }
    }

    private void siftDown(int index) {
      int leftChild = 2 * index + 1;
      int rightChild = 2 * index + 2;
      while (leftChild < size) {
        if ((rightChild < size)
            && compare(HeapArray[rightChild], HeapArray[leftChild])
            && compare(HeapArray[rightChild], HeapArray[index])) {
          swap(index, rightChild);
          index = rightChild;
        } else if (compare(HeapArray[leftChild], HeapArray[index])) {
          swap(index, leftChild);
          index = leftChild;
        } else {
          break;
        }
        leftChild = 2 * index + 1;
        rightChild = 2 * index + 2;
      }
    }

    public void insert(Allocation element) {
      HeapArray[size] = element;
      siftUp(size++);
    }

    public Allocation root() {
      return HeapArray[0];
    }

    public Allocation extract() {
      Allocation result = root();
      this.HeapArray[0] = HeapArray[--size];
      if (!isEmpty()) {
        siftDown(0);
      }
      return result;
    }

    public boolean isEmpty() {
      return size == 0;
    }

    private Allocation delete(int index) {
      long tmpLength = HeapArray[index].length;
      HeapArray[index].length = root().length + 1;
      siftUp(index);
      HeapArray[0].length = tmpLength;
      return extract();
    }

    public Allocation findByStart(long start) {
      for (int i = 0; i < size; ++i) {
        if (HeapArray[i].start == start) {
          return delete(i);
        }
      }
      return null;
    }

    public Allocation findByEnd(long end) {
      for (int i = 0; i < size; ++i) {
        if (HeapArray[i].start + HeapArray[i].length == end) {
          return delete(i);
        }
      }
      return null;
    }
  }
}
