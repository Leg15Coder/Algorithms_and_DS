package structures.basic;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static java.util.Arrays.sort;
import static org.junit.jupiter.api.Assertions.*;

class HeapTest {

  static Random rnd = new Random();

  @Test
  void insert() {
    Heap<Long> heap = new Heap<>();
    heap.insert(rnd.nextLong());
    assertFalse(heap.isEmpty());
  }

  @Test
  void root() {
    int amount = rnd.nextInt(100000);
    Long mn = null;
    Heap<Long> heap = new Heap<>();
    for (int i = 0; i < amount; ++i) {
      Long tmp = rnd.nextLong();
      if (mn == null || tmp < mn) {
        mn = tmp;
      }
      heap.insert(tmp);
    }
    assertEquals(heap.root(), mn);

    amount = rnd.nextInt(100000);
    mn = null;
    heap = new Heap<>(false);
    for (int i = 0; i < amount; ++i) {
      Long tmp = rnd.nextLong();
      if (mn == null || tmp > mn) {
        mn = tmp;
      }
      heap.insert(tmp);
    }
    assertEquals(heap.root(), mn);
  }

  @Test
  void extract() {
    int amount = rnd.nextInt(100000);
    Long[] checker = new Long[amount];
    Heap<Long> heap = new Heap<>();
    for (int i = 0; i < amount; ++i) {
      Long tmp = rnd.nextLong();
      checker[i] = tmp;
      heap.insert(tmp);
    }
    sort(checker);
    int dif = rnd.nextInt(amount);
    for (int i = 0; i < dif; ++i) {
      assertEquals(heap.extract(), checker[i]);
    }
  }

  @Test
  void size() {
    int amount = rnd.nextInt(100000);
    Heap<Long> heap = new Heap<>();
    for (int i = 0; i < amount; ++i) {
      heap.insert(rnd.nextLong());
    }
    assertEquals(heap.size(), amount);
  }

  @Test
  void clear() {
    int amount = rnd.nextInt(100000);
    Heap<Long> heap = new Heap<>();
    for (int i = 0; i < amount; ++i) {
      heap.insert(rnd.nextLong());
    }
    heap.clear();
    assertTrue(heap.isEmpty());
  }

  @Test
  void isEmpty() {
    Heap<Long> heap = new Heap<>();
    heap.insert(rnd.nextLong());
    assertFalse(heap.isEmpty());
    heap.extract();
    assertTrue(heap.isEmpty());
  }
}