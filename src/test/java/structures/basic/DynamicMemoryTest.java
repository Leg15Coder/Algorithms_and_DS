package structures.basic;

import org.junit.jupiter.api.Test;
import structures.basic.exceptions.AccessingToMemoryException;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DynamicMemoryTest {

  static Random rnd = new Random();

  @Test
  void checkIndex() {
    DynamicMemory<Long> memory = new DynamicMemory<>();
    int size = rnd.nextInt(100000);
    for (int i = 0; i < size; ++i) {
      memory.add(rnd.nextLong());
    }
    assertThrows(AccessingToMemoryException.class,() -> memory.checkIndex(-size - 100));
    assertThrows(AccessingToMemoryException.class,() -> memory.checkIndex(size + 100));
    for (int i = 0; i < 10; ++i) {
      assertTrue(memory.checkIndex(rnd.nextInt(-size, size - 1)) >= 0);
    }
  }

  @Test
  void add() {
    DynamicMemory<Long> memory = new DynamicMemory<>();
    memory.add(rnd.nextLong());
    assertTrue(memory.size > 0);
  }

  @Test
  void pop() {
    DynamicMemory<Long> memory = new DynamicMemory<>();
    int size = rnd.nextInt(100000);
    Long[] checker = new Long[size];
    for (int i = 0; i < size; ++i) {
      Long tmp = rnd.nextLong();
      checker[i] = tmp;
      memory.add(tmp);
    }
    int diff = rnd.nextInt(size);
    for (int i = 0; i < diff; ++i) {
      assertEquals(checker[size - i - 1], memory.pop());
    }
    for (int i = 0; i < size - diff; ++i) {
      assertEquals(checker[i], memory.get(i));
    }
  }

  @Test
  void get() {
    DynamicMemory<Long> memory = new DynamicMemory<>();
    int size = rnd.nextInt(24);
    Long[] checker = new Long[size];
    for (int i = 0; i < size; ++i) {
      Long tmp = rnd.nextLong();
      checker[i] = tmp;
      memory.add(tmp);
    }
    for (int i = 0; i < size; ++i) {
      assertEquals(checker[i], memory.get(i));
    }
    for (int i = 1; i <= size; ++i) {
      assertEquals(checker[size - i], memory.get(-i));
    }
  }

  @Test
  void set() {
    DynamicMemory<Long> memory = new DynamicMemory<>();
    int size = rnd.nextInt(100000);
    Long[] checker = new Long[size];
    for (int i = 0; i < size; ++i) {
      Long tmp = rnd.nextLong();
      checker[i] = rnd.nextLong();
      memory.add(tmp);
    }
    for (int i = 0; i < size; ++i) {
      memory.set(checker[i], i);
    }
    for (int i = 0; i < size; ++i) {
      assertEquals(checker[i], memory.get(i));
    }
  }

  @Test
  void getMaxSize() {
    // todo later
  }

  @Test
  void allInOne() {
    // todo later
  }

  @Test
  void getSize() {
    DynamicMemory<Long> memory = new DynamicMemory<>();
    int size = rnd.nextInt(100000);
    for (int i = 0; i < size; ++i) {
      memory.add(rnd.nextLong());
    }
    assertEquals(size, memory.getSize());
  }

  @Test
  void checkString() {
    DynamicMemory<Long> memory = new DynamicMemory<>();
    memory.add(rnd.nextLong());
    assertNotNull(memory.toString());
  }
}