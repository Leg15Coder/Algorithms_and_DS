package structures.basic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DequeTest {
  Deque<Long> deq;
  Random rnd = new Random();
  int size;

  @BeforeEach
  void init() {
    this.deq = new Deque<>();
    this.size = rnd.nextInt(1000);
  }

  @Test
  void pushFront() {
    deq.pushFront(rnd.nextLong());
    assertFalse(deq.isEmpty());
  }

  @Test
  void isEmpty() {
    assertTrue(deq.isEmpty());
    deq.pushFront(rnd.nextLong());
    deq.pushBack(rnd.nextLong());
    assertFalse(deq.isEmpty());
  }

  @Test
  void getSize() {
    for (int i = 0; i < size; ++i) {
      deq.pushFront(rnd.nextLong());
      deq.pushBack(rnd.nextLong());
    }
    assertEquals(2 * size, deq.getSize());

    int n = rnd.nextInt(size);
    for (int i = 0; i < n; ++i) {
      deq.popFront();
      deq.popBack();
    }
    assertEquals(2 * (size - n), deq.getSize());
  }

  @Test
  void pushBack() {
    deq.pushBack(rnd.nextLong());
    assertFalse(deq.isEmpty());
  }

  @Test
  void back() {
    long last = 0;
    assertNull(deq.back());
    for (int i = 0; i < size; ++i) {
      deq.pushFront(rnd.nextLong());
      last = rnd.nextLong();
      deq.pushBack(last);
    }

    assertEquals(last, deq.back());
  }

  @Test
  void front() {
    long first = 0;
    assertNull(deq.front());
    for (int i = 0; i < size; ++i) {
      deq.pushBack(rnd.nextLong());
      first = rnd.nextLong();
      deq.pushFront(first);
    }

    assertEquals(first, deq.front());
  }

  @Test
  void popBack() {
    long[] toCheck = new long[2 * size];
    for (int i = 0; i < size; ++i) {
      long last = rnd.nextLong();
      deq.pushBack(last);
      long first = rnd.nextLong();
      deq.pushFront(first);

      toCheck[size - i - 1] = first;
      toCheck[size + i] = last;
    }

    for (int i = 2 * size - 1; i >= 0; --i) {
      assertEquals(toCheck[i], deq.popBack());
    }
    assertThrows(IllegalStateException.class, () -> deq.popBack());
  }

  @Test
  void popFront() {
    long[] toCheck = new long[2 * size];
    for (int i = 0; i < size; ++i) {
      long last = rnd.nextLong();
      deq.pushBack(last);
      long first = rnd.nextLong();
      deq.pushFront(first);

      toCheck[size - i - 1] = first;
      toCheck[size + i] = last;
    }

    for (int i = 0; i < 2 * size; ++i) {
      assertEquals(toCheck[i], deq.popFront());
    }
    assertThrows(IllegalStateException.class, () -> deq.popFront());
  }
}