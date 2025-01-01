package structures.arrays;

import org.junit.jupiter.api.Test;
import structures.basic.Deque;
import structures.basic.Stack;

import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


public abstract class ArrayTest {
  Random rnd = new Random();
  ArrayInterface<Long> arr;
  int size;

  @Test
  void getAt() {
    long[] toCheck = new long[size];
    for (int i = 0; i < size; ++i) {
      var tmp = rnd.nextLong();
      toCheck[i] = tmp;
      arr.insert(tmp);
    }

    for (int i = 0; i < size; ++i) {
      assertEquals(toCheck[i], arr.getAt(i));
      assertEquals(toCheck[i], arr.getAt(-size+i));
    }

    for (int i = size; i <= 2 * size; ++i) {
      int finalI = i;
      assertThrows(IndexOutOfBoundsException.class, () -> arr.getAt(finalI));
    }
    for (int i = size + 1; i <= 2 * size; ++i) {
      int finalI = i;
      assertThrows(IndexOutOfBoundsException.class, () -> arr.getAt(-finalI));
    }
  }

  @Test
  void first() {
    Long first = 0L;

    for (int i = 0; i < size; ++i) {
      var tmp = rnd.nextLong();
      if (i == 0) {
        first = tmp;
      }
      arr.insert(tmp);
    }

    assertEquals(first, arr.first());
  }

  @Test
  void last() {
    Long last = 0L;

    for (int i = 0; i < size; ++i) {
      var tmp = rnd.nextLong();
      if (i == size - 1) {
        last = tmp;
      }
      arr.insert(tmp);
    }

    assertEquals(last, arr.last());
  }

  @Test
  void setAt() {
    long[] toCheck = new long[size];
    for (int i = 0; i < size; ++i) {
      var tmp = rnd.nextLong();
      toCheck[i] = tmp;
      arr.insert(tmp);
    }

    for (int i = 0; i < size; ++i) {
      var tmp = rnd.nextLong();
      toCheck[i] = tmp;
      arr.setAt(i, tmp);
    }

    for (int i = 0; i < size; ++i) {
      assertEquals(toCheck[i], arr.getAt(i));
    }
  }

  @Test
  void getIndex() {
    long[] toCheck = new long[size + 1];
    assertThrows(NoSuchElementException.class, () -> arr.getIndex(rnd.nextLong()));
    for (int i = 0; i < size; ++i) {
      var tmp = rnd.nextLong();
      boolean flag = true;
      while (flag) {
        flag = false;
        for (var e : toCheck) {
          if (e == tmp) {
            tmp = rnd.nextLong();
            flag = true;
            break;
          }
        }
      }
      toCheck[i] = tmp;
      arr.insert(tmp);
    }

    for (int i = 0; i < size; ++i) {
      assertEquals(i, arr.getIndex(toCheck[i]));
    }
    assertThrows(NoSuchElementException.class, () -> arr.getIndex(toCheck[size]));
  }

  @Test
  void pop() {
    long[] toCheck = new long[size];
    for (int i = 0; i < size; ++i) {
      var tmp = rnd.nextLong();
      toCheck[i] = tmp;
      arr.insert(tmp);
    }

    int n = rnd.nextInt(size);
    for (int i = 0; i < n; ++i) {
      assertEquals(toCheck[size - 1 - i], arr.pop());
    }

    for (int i = 0; i < size - n; ++i) {
      assertEquals(toCheck[i], arr.getAt(i));
    }
  }

  @Test
  void remove() {
    long[] toCheck = new long[size];
    for (int i = 0; i < size; ++i) {
      var tmp = rnd.nextLong();
      toCheck[i] = tmp;
      arr.insert(tmp);
    }

    int r = rnd.nextInt(1, size);
    int l = rnd.nextInt(r);

    for (int i = l; i <= r; ++i) {
      arr.remove(l);
    }

    for (int i = 0; i < size - r + l - 1; ++i) {
      if (i < l) {
        assertEquals(toCheck[i], arr.getAt(i));
      } else {
        assertEquals(toCheck[i + r - l + 1], arr.getAt(i));
      }
    }
  }

  @Test
  void testRemove() {
    long[] toCheck = new long[size];
    for (int i = 0; i < size; ++i) {
      var tmp = rnd.nextLong();
      boolean flag = true;
      while (flag) {
        flag = false;
        for (var e : toCheck) {
          if (e == tmp) {
            tmp = rnd.nextLong();
            flag = true;
            break;
          }
        }
      }
      toCheck[i] = tmp;
      arr.insert(tmp);
    }

    int r = rnd.nextInt(1, size);
    int l = rnd.nextInt(r);

    for (int i = l; i <= r; ++i) {
      assertTrue(arr.remove(toCheck[i]));
    }

    for (int i = 0; i < size - r + l - 1; ++i) {
      if (i < l) {
        assertEquals(toCheck[i], arr.getAt(i));
      } else {
        assertEquals(toCheck[i + r - l + 1], arr.getAt(i));
      }
    }

    arr.clear();

    toCheck = new long[size];
    for (int i = 0; i < size; ++i) {
      var tmp = rnd.nextLong(-100, 100);
      toCheck[i] = tmp;
      arr.insert(tmp);
    }

    arr.remove(toCheck[size - 1]);
    boolean flag = false;
    for (int i = 0; i < size; ++i) {
      if (toCheck[i] == toCheck[size - 1]) {
        flag = true;
      }

      if (i == size - 1) {
        assertEquals(size - 1, arr.getSize());
      } else {
        if (flag) {
          assertEquals(toCheck[i + 1], arr.getAt(i));
        } else {
          assertEquals(toCheck[i], arr.getAt(i));
        }
      }
    }
  }

  @Test
  void insert() {
    var tmp = rnd.nextLong();
    arr.insert(tmp);

    assertFalse(arr.isEmpty());
    assertEquals(arr.first(), tmp);
    assertEquals(arr.last(), tmp);
  }

  @Test
  void testInsert() {
    Deque<Long> toCheck = new Deque<>();
    for (int i = 0; i < size; ++i) {
      var tmp = rnd.nextLong();

      int index = rnd.nextInt(i + 1);
      Stack<Long> st = new Stack<>();
      for (int j = 0; j < i - index; ++j) {
        st.pushFront(toCheck.popBack());
      }
      toCheck.pushBack(tmp);
      while (!st.isEmpty()) {
        toCheck.pushBack(st.popFront());
      }

      arr.insert(tmp, index);
    }

    for (int i = 0; i < size; ++i) {
      assertEquals(toCheck.popFront(), arr.getAt(i));
    }

    for (int i = size + 1; i <= 2 * size; ++i) {
      int finalI = i;
      assertThrows(IndexOutOfBoundsException.class, () -> arr.insert(rnd.nextLong(), finalI));
    }
    for (int i = size + 2; i <= 2 * size; ++i) {
      int finalI = i;
      assertThrows(IndexOutOfBoundsException.class, () -> arr.insert(rnd.nextLong(), -finalI));
    }
  }

  @Test
  void getSize() {
    for (int i = 0; i < size; ++i) {
      arr.insert(rnd.nextLong());
    }
    assertEquals(size, arr.getSize());

    int n = rnd.nextInt(size);
    for (int i = 0; i < n; ++i) {
      arr.pop();
    }
    assertEquals(size - n, arr.getSize());
  }

  @Test
  void clear() {
    for (int i = 0; i < size; ++i) {
      arr.insert(rnd.nextLong());
    }
    arr.clear();
    assertTrue(arr.isEmpty());
  }

  @Test
  void isEmpty() {
    assertTrue(arr.isEmpty());
    arr.insert(rnd.nextLong());
    assertFalse(arr.isEmpty());
    arr.clear();
    assertTrue(arr.isEmpty());
  }

  @Test
  void checkString() {
    assertNotNull(arr.toString());
  }
}
