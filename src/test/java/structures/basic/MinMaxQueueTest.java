package structures.basic;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MinMaxQueueTest {

  static Random rnd = new Random();

  @Test
  void pushFront() {
    MinMaxQueue<Long> queue = new MinMaxQueue<>();
    queue.pushFront(rnd.nextLong());
    assertFalse(queue.isEmpty());
  }

  @Test
  void back() {
    int amount = rnd.nextInt(100000);
    MinMaxQueue<Long> queue = new MinMaxQueue<>();
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
    MinMaxQueue<Long> queue = new MinMaxQueue<>();
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
    MinMaxQueue<Long> queue = new MinMaxQueue<>();
    queue.pushFront(rnd.nextLong());
    assertNotNull(queue.back());
  }

  @Test
  void getSize() {
    int amount = rnd.nextInt(100000);
    MinMaxQueue<Long> queue = new MinMaxQueue<>();
    for (int i = 0; i < amount; ++i) {
      queue.pushFront(rnd.nextLong());
    }
    assertEquals(queue.getSize(), amount);
  }

  @Test
  void getMax() {
    int amount = rnd.nextInt(100000);
    MinMaxQueue<Long> queue = new MinMaxQueue<>();
    Long mx = null;
    for (int i = 0; i < amount; ++i) {
      Long tmp = rnd.nextLong();
      if (mx == null || tmp > mx) {
        mx = tmp;
      }
      queue.pushFront(tmp);
    }
    assertEquals(queue.getMax(), mx);
  }

  @Test
  void getMin() {
    int amount = rnd.nextInt(100000);
    MinMaxQueue<Long> queue = new MinMaxQueue<>();
    Long mn = null;
    for (int i = 0; i < amount; ++i) {
      Long tmp = rnd.nextLong();
      if (mn == null || tmp < mn) {
        mn = tmp;
      }
      queue.pushFront(tmp);
    }
    assertEquals(queue.getMin(), mn);
  }
}