package structures.basic;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class QueueTest {

  static Random rnd = new Random();

  @Test
  void pushFront() {
    Queue<Long> queue = new Queue<>();
    queue.pushFront(rnd.nextLong());
    assertFalse(queue.isEmpty());
  }

  @Test
  void back() {
    int amount = rnd.nextInt(100000);
    Queue<Long> queue = new Queue<>();
    Long first = rnd.nextLong();
    queue.pushFront(first);
    for (int i = 0; i < amount - 1; ++i) {
      queue.pushFront(rnd.nextLong());
    }
    assertEquals(queue.back(), first);
  }

  @Test
  void popBack() {
    int amount = rnd.nextInt(100000);
    Queue<Long> queue = new Queue<>();
    Long[] checker = new Long[amount];
    for (int i = 0; i < amount; ++i) {
      Long tmp = rnd.nextLong();
      checker[i] = tmp;
      queue.pushFront(tmp);
    }
    for (int i = 0; i < amount; ++i) {
      assertEquals(queue.popBack(), checker[i]);
    }
    for (int i = 0; i < rnd.nextInt(100); ++i) {
      assertThrows(IllegalStateException.class, queue::popBack);
      assertNull(queue.back());
    }
  }

  @Test
  void isEmpty() {
    Queue<Long> queue = new Queue<>();
    queue.pushFront(rnd.nextLong());
    assertNotNull(queue.back());
  }

  @Test
  void getSize() {
    int amount = rnd.nextInt(100000);
    Queue<Long> queue = new Queue<>();
    for (int i = 0; i < amount; ++i) {
      queue.pushFront(rnd.nextLong());
    }
    assertEquals(queue.getSize(), amount);
  }
}