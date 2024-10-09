package tmp.tests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class Main {
  public static <T extends Comparable<T>> T min(T a, T b) {
    return (a.compareTo(b) > 0) ? b : a;
  }

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

  public static class MemoryManager {
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
          current =
              new Allocation(min(ToCheck.start, current.start), ToCheck.length + current.length);
        } else {
          tmp.insert(ToCheck);
        }
      }
      tmp.insert(current);
      this.memory = tmp;
    }
  }

  public static class Heap<T extends Comparable<T>> {
    private final Object[] HeapArray;
    private boolean IsMin = true;
    private int size = 0;

    public Heap(int maxSize, boolean IsMin) {
      this.HeapArray = new Object[maxSize];
      this.IsMin = IsMin;
    }

    public Heap(int maxSize) {
      this.HeapArray = new Object[maxSize];
    }

    public Heap(boolean IsMin) {
      this.HeapArray = new Object[1 << 27];
      this.IsMin = IsMin;
    }

    public Heap() {
      this.HeapArray = new Object[1 << 27];
    }

    private boolean compare(T left, T right) {
      if (IsMin) {
        return left.compareTo(right) < 0;
      } else {
        return left.compareTo(right) > 0;
      }
    }

    private void swap(int left, int right) {
      T tmp = (T) HeapArray[left];
      this.HeapArray[left] = HeapArray[right];
      this.HeapArray[right] = tmp;
    }

    private void siftUp(int index) {
      while (index > 0) {
        int parent = (index - 1) / 2;
        if (compare((T) HeapArray[index], (T) HeapArray[parent])) {
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
            && compare((T) HeapArray[rightChild], (T) HeapArray[leftChild])
            && compare((T) HeapArray[rightChild], (T) HeapArray[index])) {
          swap(index, rightChild);
          index = rightChild;
        } else if (compare((T) HeapArray[leftChild], (T) HeapArray[index])) {
          swap(index, leftChild);
          index = leftChild;
        } else {
          break;
        }
        leftChild = 2 * index + 1;
        rightChild = 2 * index + 2;
      }
    }

    public void insert(T element) {
      HeapArray[size] = element;
      siftUp(size++);
    }

    public T root() {
      return (T) HeapArray[0];
    }

    public T extract() {
      T result = root();
      this.HeapArray[0] = HeapArray[--size];
      if (!isEmpty()) {
        siftDown(0);
      }
      return result;
    }

    public int size() {
      return size;
    }

    public void clear() {
      size = 0;
    }

    public boolean isEmpty() {
      return size == 0;
    }
  }
}
