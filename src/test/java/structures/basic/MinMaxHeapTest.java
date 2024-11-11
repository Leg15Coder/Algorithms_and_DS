package structures.basic;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static java.util.Arrays.sort;
import static org.junit.jupiter.api.Assertions.*;

class MinMaxHeapTest {

  static Random rnd = new Random();

  @Test
  void insert() {
    MinMaxHeap<Long> heap = new MinMaxHeap<>();
    heap.insert(rnd.nextLong());
    assertFalse(heap.isEmpty());
  }

  @Test
  void extract_min() {
    int amount = rnd.nextInt(100000);
    Long[] checker = new Long[amount];
    MinMaxHeap<Long> heap = new MinMaxHeap<>();
    for (int i = 0; i < amount; ++i) {
      Long tmp = rnd.nextLong();
      checker[i] = tmp;
      heap.insert(tmp);
    }
    sort(checker);
    int dif = rnd.nextInt(amount);
    for (int i = 0; i < dif; ++i) {
      assertEquals(heap.extract_min(), checker[i]);
    }
  }

  @Test
  void extract_max() {
    int amount = rnd.nextInt(100000);
    Long[] checker = new Long[amount];
    MinMaxHeap<Long> heap = new MinMaxHeap<>();
    for (int i = 0; i < amount; ++i) {
      Long tmp = rnd.nextLong();
      checker[i] = tmp;
      heap.insert(tmp);
    }
    sort(checker);
    int dif = rnd.nextInt(amount);
    for (int i = 0; i < dif; ++i) {
      assertEquals(heap.extract_max(), checker[amount - i - 1]);
    }
  }

  @Test
  void get_min() {
    int amount = rnd.nextInt(100000);
    Long mn = null;
    MinMaxHeap<Long> heap = new MinMaxHeap<>();
    for (int i = 0; i < amount; ++i) {
      Long tmp = rnd.nextLong();
      if (mn == null || tmp < mn) {
        mn = tmp;
      }
      heap.insert(tmp);
    }
    assertEquals(heap.get_min(), mn);
  }

  @Test
  void get_max() {
    int amount = rnd.nextInt(100000);
    Long mn = null;
    MinMaxHeap<Long> heap = new MinMaxHeap<>();
    for (int i = 0; i < amount; ++i) {
      Long tmp = rnd.nextLong();
      if (mn == null || tmp > mn) {
        mn = tmp;
      }
      heap.insert(tmp);
    }
    assertEquals(heap.get_max(), mn);
  }

  @Test
  void size() {
    int amount = rnd.nextInt(100000);
    MinMaxHeap<Long> heap = new MinMaxHeap<>();
    for (int i = 0; i < amount; ++i) {
      heap.insert(rnd.nextLong());
    }
    assertEquals(heap.size(), amount);
  }

  @Test
  void clear() {
    int amount = rnd.nextInt(100000);
    MinMaxHeap<Long> heap = new MinMaxHeap<>();
    for (int i = 0; i < amount; ++i) {
      heap.insert(rnd.nextLong());
    }
    heap.clear();
    assertTrue(heap.isEmpty());
  }

  @Test
  void isEmpty() {
    MinMaxHeap<Long> heap = new MinMaxHeap<>();
    heap.insert(rnd.nextLong());
    assertFalse(heap.isEmpty());
    heap.extract_min();
    assertTrue(heap.isEmpty());
  }
}