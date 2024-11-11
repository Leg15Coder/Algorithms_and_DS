package structures.trees.search;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Random;

import static java.util.Arrays.sort;
import static org.junit.jupiter.api.Assertions.*;

class TreapTest {

  Random rnd = new Random();

  @Test
  void add() {
    Treap<Long> tree = new Treap<>();
    tree.add(rnd.nextLong());
    assertNotNull(tree.root);
  }

  @Test
  void remove() {
    int sizeUniq = rnd.nextInt(10000);
    int sizeNotUniq = rnd.nextInt(10000);
    int n = rnd.nextInt(sizeUniq);
    int m = rnd.nextInt(sizeNotUniq);
    Long[] toCheckUniq = new Long[n];
    Long[] toCheckNotUniq = new Long[m];
    Treap<Long> tree = new Treap<>();
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
      assertTrue(tree.remove(toCheck));
    }
    for (Long toCheck : toCheckUniq) {
      assertTrue(tree.remove(toCheck));
      assertFalse(tree.remove(toCheck));
    }
  }

  @Test
  void get() {
    int size = rnd.nextInt(1000);
    int n = rnd.nextInt(size);
    long[] toCheck = new long[n+1];
    toCheck[0] = rnd.nextLong();
    Treap<Long> tree = new Treap<>();
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
  void next() {
    int size = rnd.nextInt(1000);
    long[] toCheck = new long[size];
    Treap<Long> tree = new Treap<>();
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
    int size = rnd.nextInt(1000);
    long[] toCheck = new long[size];
    Treap<Long> tree = new Treap<>();
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
    Treap<Long> tree = new Treap<>();
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
    int size = rnd.nextInt(1000);
    Long mn = null;
    Treap<Long> tree = new Treap<>();
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
    int size = rnd.nextInt(1000);
    Long mx = null;
    Treap<Long> tree = new Treap<>();
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
    Treap<Long> tree = new Treap<>();
    tree.add(rnd.nextLong());
    tree.clear();
    assertNull(tree.root);
  }

  @Test
  void clear() {
    int size = rnd.nextInt(1000);
    Treap<Long> tree = new Treap<>();
    for (int i = 0; i < size; ++i) {
      tree.add(rnd.nextLong());
    }
    tree.clear();
    assertTrue(tree.isEmpty());
  }

  @Test
  void getSize() {
    Treap<Long> tree = new Treap<>();
    assertEquals(tree.getSize(), 0);

    // todo later
  }
}