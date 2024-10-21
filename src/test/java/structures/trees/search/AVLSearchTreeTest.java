package structures.trees.search;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AVLSearchTreeTest {


  Random rnd = new Random();

  @Test
  void add() {
    AVLSearchTree<Long> tree = new AVLSearchTree<>();
    tree.add(rnd.nextLong());
    assertNotNull(tree.root);
  }

  @Test
  void remove() {
    int sizeUniq = rnd.nextInt(1000);
    int sizeNotUniq = rnd.nextInt(1000);
    int n = rnd.nextInt(sizeUniq);
    int m = rnd.nextInt(sizeNotUniq);
    Long[] toCheckUniq = new Long[n];
    Long[] toCheckNotUniq = new Long[m];
    AVLSearchTree<Long> tree = new AVLSearchTree<>();
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
    AVLSearchTree<Long> tree = new AVLSearchTree<>();
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
  void getMin() {
    int size = rnd.nextInt(1000);
    Long mn = null;
    AVLSearchTree<Long> tree = new AVLSearchTree<>();
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
    AVLSearchTree<Long> tree = new AVLSearchTree<>();
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
    AVLSearchTree<Long> tree = new AVLSearchTree<>();
    tree.add(rnd.nextLong());
    tree.clear();
    assertNull(tree.root);
  }

  @Test
  void clear() {
    int size = rnd.nextInt(1000);
    AVLSearchTree<Long> tree = new AVLSearchTree<>();
    for (int i = 0; i < size; ++i) {
      tree.add(rnd.nextLong());
    }
    tree.clear();
    assertTrue(tree.isEmpty());
  }

  @Test
  void next() {
    int size = rnd.nextInt(1000);
    int n = rnd.nextInt(size);
    long[] toCheck = new long[n];
    Long mx = null;
    AVLSearchTree<Long> tree = new AVLSearchTree<>();
    for (int i = 0; i < size; ++i) {
      long tmp = rnd.nextLong();
      if (mx == null || tmp > mx) {
        mx = tmp;
      }
      tree.add(tmp * 2);
      if (i < n) {
        toCheck[i] = tmp * 2;
      }
    }
    for (int i = 0; i < n; ++i) {
      assertEquals(tree.next(toCheck[i] - 1), toCheck[i]);
      assertEquals(tree.next(toCheck[i] + 1), toCheck[i]);
    }
    assertEquals(tree.next(mx), mx);
    assertNull(tree.next(mx + 1));
  }

  @Test
  void testAdd() {
    assertNotNull(null); // todo later
  }

  @Test
  void testRemove() {
    assertNotNull(null); // todo later
  }
}