package structures.basic;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class StackTest {

  static Random rnd = new Random();

  @Test
  void front() {
    int amount = rnd.nextInt(100000);
    Stack<Long> stack = new Stack<>();
    Long last = null;
    for (int i = 0; i < amount; ++i) {
      Long tmp = rnd.nextLong();
      last = tmp;
      stack.pushFront(tmp);
    }
    assertEquals(stack.front(), last);
  }

  @Test
  void pushFront() {
    Stack<Long> stack = new Stack<>();
    stack.pushFront(rnd.nextLong());
    assertFalse(stack.isEmpty());
  }

  @Test
  void popFront() {
    int amount = rnd.nextInt(100000);
    Stack<Long> stack = new Stack<>();
    Long[] checker = new Long[amount];
    for (int i = 0; i < amount; ++i) {
      Long tmp = rnd.nextLong();
      checker[i] = tmp;
      stack.pushFront(tmp);
    }
    for (int i = 0; i < amount; ++i) {
      assertEquals(stack.popFront(), checker[amount - i - 1]);
    }
    for (int i = 0; i < rnd.nextInt(100); ++i) {
      assertThrows(IllegalStateException.class, stack::popFront);
      assertNull(stack.front());
    }
  }

  @Test
  void isEmpty() {
    Stack<Long> stack = new Stack<>();
    stack.pushFront(rnd.nextLong());
    assertNotNull(stack.front());
  }

  @Test
  void getSize() {
    int amount = rnd.nextInt(100000);
    Stack<Long> stack = new Stack<>();
    for (int i = 0; i < amount; ++i) {
      stack.pushFront(rnd.nextLong());
    }
    assertEquals(stack.getSize(), amount);
  }
}