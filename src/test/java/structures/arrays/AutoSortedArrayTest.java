package structures.arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Random;

import static java.util.Arrays.sort;
import static org.junit.jupiter.api.Assertions.*;

class AutoSortedArrayTest {

  Random rnd = new Random();

  AutoSortedArray<Long> tree;
  int size;

  @BeforeEach
  void init() {
    tree = new AutoSortedArray<>();
    size = rnd.nextInt(1000);
  }

  @Test
  void add() {
    tree.add(rnd.nextLong());
    assertEquals(1, tree.getSize());
  }

  @Test
  void remove() {
    int sizeUniq = rnd.nextInt(10000);
    int sizeNotUniq = rnd.nextInt(10000);
    int n = rnd.nextInt(sizeUniq);
    int m = rnd.nextInt(sizeNotUniq);
    Long[] toCheckUniq = new Long[n];
    Long[] toCheckNotUniq = new Long[m];

    for (int i = 0; i < sizeUniq + sizeNotUniq; ++i) {
      Long tmp = rnd.nextLong();
      boolean flag = true;

      while (flag) {
        flag = false;
        for (Long toCheck : toCheckUniq) {
          if (Objects.equals(toCheck, tmp)) {
            flag = true;
            break;
          }
        }
        tmp = rnd.nextLong();
      }

      tree.add(tmp);
      if (i >= sizeUniq) {
        tree.add(tmp);
      }
      if (i < n) {
        toCheckUniq[i] = tmp;
      }
      if (i >= sizeUniq + sizeNotUniq - m) {
        toCheckNotUniq[i - (sizeUniq + sizeNotUniq - m)] = tmp;
      }

    }

    for (Long toCheck : toCheckNotUniq) {
      assertTrue(tree.remove(toCheck));
      // assertTrue(tree.remove(toCheck)); todo later
    }
    for (Long toCheck : toCheckUniq) {
      assertTrue(tree.remove(toCheck));
      assertFalse(tree.remove(toCheck));
    }
  }

  @Test
  void get() {
    int n = rnd.nextInt(size);
    long[] toCheck = new long[n+1];
    toCheck[0] = rnd.nextLong();

    for (int i = 0; i < size; ++i) {
      long tmp = rnd.nextLong();
      while (tmp == toCheck[0]) {
        tmp = rnd.nextLong();
      }
      tree.add(tmp);
      if (i < n) {
        toCheck[i+1] = tmp;
      }
    }

    assertFalse(tree.get(toCheck[0]));
    for (int i = 1; i <= n; ++i) {
      assertTrue(tree.get(toCheck[i]));
    }
  }

  @Test
  void last() {
    Long last = null;

    for (int i = 0; i < size; ++i) {
      Long tmp = rnd.nextLong();
      if (last == null || tmp.compareTo(last) > 0) {
        last = tmp;
      }
      tree.add(last);
    }

    assertEquals(last, tree.last());
  }

  @Test
  void setAt() {
    int n = rnd.nextInt(size);
    for (int i = 0; i < size; ++i) {
      tree.add(rnd.nextLong());
    }

    for (int i = 0; i < n; ++i) {
      int finalI = i;
      assertThrows(UnsupportedOperationException.class, () -> tree.setAt(finalI, rnd.nextLong()));
    }
  }

  @Test
  void next() {
    long[] toCheck = new long[size];
    assertNull(tree.next(rnd.nextLong()));

    for (int i = 0; i < size; ++i) {
      long tmp = rnd.nextLong() << 1;
      tree.add(tmp);
      toCheck[i] = tmp;
    }

    sort(toCheck);

    for (int i = 0; i < size; ++i) {
      assertEquals(tree.next(toCheck[i]), toCheck[i]);
      assertEquals(tree.next(toCheck[i] - 1), toCheck[i]);
    }
    assertNull(tree.next(toCheck[size - 1] + 1));
  }

  @Test
  void previous() {
    long[] toCheck = new long[size];
    assertNull(tree.previous(rnd.nextLong()));

    for (int i = 0; i < size; ++i) {
      long tmp = rnd.nextLong() << 1;
      tree.add(tmp);
      toCheck[i] = tmp;
    }

    sort(toCheck);

    for (int i = 0; i < size; ++i) {
      assertEquals(tree.previous(toCheck[i]), toCheck[i]);
      assertEquals(tree.previous(toCheck[i] + 1), toCheck[i]);
    }
    assertNull(tree.previous(toCheck[0] - 1));
  }

  @Test
  void delete() {
    int sizeUniq = rnd.nextInt(10000);
    int sizeNotUniq = rnd.nextInt(10000);
    int n = rnd.nextInt(sizeUniq);
    int m = rnd.nextInt(sizeNotUniq);
    Long[] toCheckUniq = new Long[n];
    Long[] toCheckNotUniq = new Long[m];

    for (int i = 0; i < sizeUniq + sizeNotUniq; ++i) {
      Long tmp = rnd.nextLong();
      boolean flag = true;

      while (flag) {
        flag = false;
        for (Long toCheck : toCheckUniq) {
          if (Objects.equals(toCheck, tmp)) {
            flag = true;
            break;
          }
        }
        tmp = rnd.nextLong();
      }

      tree.add(tmp);
      if (i >= sizeUniq) {
        tree.add(tmp);
      }
      if (i < n) {
        toCheckUniq[i] = tmp;
      }
      if (i >= sizeUniq + sizeNotUniq - m) {
        toCheckNotUniq[i - (sizeUniq + sizeNotUniq - m)] = tmp;
      }
    }

    for (Long toCheck : toCheckNotUniq) {
      assertTrue(tree.delete(toCheck));
      assertFalse(tree.delete(toCheck));
    }
    for (Long toCheck : toCheckUniq) {
      assertTrue(tree.delete(toCheck));
      assertFalse(tree.delete(toCheck));
    }
  }

  @Test
  void getMin() {
    Long mn = null;
    assertNull(tree.getMin());

    for (int i = 0; i < size; ++i) {
      Long tmp = rnd.nextLong();
      if (mn == null || tmp.compareTo(mn) < 0) {
        mn = tmp;
      }
      tree.add(tmp);
    }

    assertEquals(tree.getMin(), mn);
  }

  @Test
  void getMax() {
    Long mx = null;
    assertNull(tree.getMax());

    for (int i = 0; i < size; ++i) {
      Long tmp = rnd.nextLong();
      if (mx == null || tmp.compareTo(mx) > 0) {
        mx = tmp;
      }
      tree.add(tmp);
    }

    assertEquals(tree.getMax(), mx);
  }

  @Test
  void isEmpty() {
    tree.add(rnd.nextLong());
    tree.clear();
    assertEquals(0, tree.getSize());
  }

  @Test
  void clear() {
    for (int i = 0; i < size; ++i) {
      tree.add(rnd.nextLong());
    }
    tree.clear();
    assertTrue(tree.isEmpty());
  }

  @Test
  void getSize() {
    assertEquals(tree.getSize(), 0);
    for (int i = 0; i < size; ++i) {
      tree.add(rnd.nextLong());
    }
    assertEquals(tree.getSize(), size);
  }

  @Test
  void testRemove() {
    long[] toCheck = new long[size];
    for (int i = 0; i < size; ++i) {
      long tmp = rnd.nextLong();
      tree.add(tmp);
      toCheck[i] = tmp;
    }

    sort(toCheck);

    int right = rnd.nextInt(size);
    int left = rnd.nextInt(right);
    for (int i = 0; i < right - left + 1; ++i) {
      tree.remove(left);
    }

    for (int i = 0; i < size - right + left - 1; ++i) {
      if (i < left) {
        assertEquals(i, tree.getIndex(toCheck[i]));
      } else {
        assertEquals(i, tree.getIndex(toCheck[i + right - left + 1]));
      }
    }
  }

  @Test
  void insert() {
    assertThrows(UnsupportedOperationException.class, () -> tree.insert(rnd.nextLong(), rnd.nextInt()));
  }

  @Test
  void testInsert() {
    tree.insert(rnd.nextLong());
    assertEquals(1, tree.getSize());
  }

  @Test
  void sum() {
    long[] toCheck = new long[size];

    for (int i = 0; i < size; ++i) {
      long tmp = rnd.nextLong();
      tree.add(tmp);
      toCheck[i] = tmp;
    }

    sort(toCheck);

    int right = rnd.nextInt(size);
    int left = rnd.nextInt(right);
    long result = 0;
    for (int i = left; i <= right; ++i) {
      result += toCheck[i];
    }
    // assertEquals(result, tree.sum(left, right)); todo later
  }

  @Test
  void getAt() {
    long[] toCheck = new long[size];
    for (int i = 0; i < size; ++i) {
      long tmp = rnd.nextLong();
      tree.add(tmp);
      toCheck[i] = tmp;
    }

    sort(toCheck);

    for (int i = 0; i < size; ++i) {
      assertEquals(toCheck[i], tree.getAt(i));
    }
    for (int i = 1; i <= size; ++i) {
      assertEquals(toCheck[size - i], tree.getAt(-i));
    }

    for (int i = size; i <= 2 * size; ++i) {
      int finalI = i;
      assertThrows(IndexOutOfBoundsException.class, () -> tree.getAt(finalI));
    }
    for (int i = size + 1; i <= 2 * size; ++i) {
      int finalI = i;
      assertThrows(IndexOutOfBoundsException.class, () -> tree.getAt(-finalI));
    }
  }

  @Test
  void getIndex() {
    long[] toCheck = new long[size];
    for (int i = 0; i < size; ++i) {
      long tmp = rnd.nextLong();
      tree.add(tmp);
     toCheck[i] = tmp;
    }

    sort(toCheck);

    for (int i = 0; i < size; ++i) {
      assertEquals(i, tree.getIndex(toCheck[i]));
    }
    assertThrows(IllegalArgumentException.class, () -> tree.getIndex(toCheck[toCheck.length - 1] + 1));
    assertThrows(IllegalArgumentException.class, () -> tree.getIndex(toCheck[0] - 1));
  }

  @Test
  void pop() {
    long[] toCheck = new long[size];
    for (int i = 0; i < size; ++i) {
      long tmp = rnd.nextLong();
      tree.add(tmp);
      toCheck[i] = tmp;
    }

    sort(toCheck);

    for(int i = 1; i <= size; ++i) {
      assertEquals(toCheck[size - i], tree.pop());
    }
    assertThrows(UnsupportedOperationException.class, tree::pop);
  }
}