package structures.trees.search;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AVLSetTest {


  Random rnd = new Random();

  @Test
  void add() {
    AVLSet<Long> tree = new AVLSet<>();
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
    AVLSet<Long> tree = new AVLSet<>();
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
      assertFalse(tree.remove(toCheck));
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
    AVLSet<Long> tree = new AVLSet<>();
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
    AVLSet<Long> tree = new AVLSet<>();
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
    AVLSet<Long> tree = new AVLSet<>();
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
    AVLSet<Long> tree = new AVLSet<>();
    tree.add(rnd.nextLong());
    tree.clear();
    assertNull(tree.root);
  }

  @Test
  void clear() {
    int size = rnd.nextInt(1000);
    AVLSet<Long> tree = new AVLSet<>();
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
    AVLSet<Long> tree = new AVLSet<>();
    for (int i = 0; i < size; ++i) {
      long tmp = rnd.nextLong(-1000000000, 1000000000);
      if (mx == null || tmp * 2 > mx) {
        mx = tmp * 2;
      }
      tree.add(tmp * 2);
      if (i < n) {
        toCheck[i] = tmp * 2;
      }
    }
    for (int i = 0; i < n; ++i) {
      assertEquals(tree.next(toCheck[i] - 1), toCheck[i]);
      assertEquals(tree.next(toCheck[i]), toCheck[i]);
    }
    assertEquals(tree.next(mx), mx);
    assertNull(tree.next(mx + 1));
  }

  @Test
  void previous() {
    int size = rnd.nextInt(1000);
    int n = rnd.nextInt(size);
    long[] toCheck = new long[n];
    Long mn = null;
    AVLSet<Long> tree = new AVLSet<>();
    for (int i = 0; i < size; ++i) {
      long tmp = rnd.nextLong(-1000000000, 1000000000);
      if (mn == null || tmp * 2 < mn) {
        mn = tmp * 2;
      }
      tree.add(tmp * 2);
      if (i < n) {
        toCheck[i] = tmp * 2;
      }
    }
    for (int i = 0; i < n; ++i) {
      assertEquals(tree.previous(toCheck[i] + 1), toCheck[i]);
      assertEquals(tree.previous(toCheck[i]), toCheck[i]);
    }
    assertEquals(tree.previous(mn), mn);
    assertNull(tree.previous(mn - 1));
  }

  @Test
  void delete() {
    remove();
  }

  @Test
  void testAdd() {
    AVLSet<Long> tree = new AVLSet<>();
    for (int i = 0; i < (1 << 18); ++i) {
      tree.add(rnd.nextLong());
    }
    int dif = tree.root.diff();
    assertTrue(dif > -2 && dif < 2);
    tree.clear();
    for (long i = 0; i < 100; ++i) {
      tree.add(i);
    }
    dif = tree.root.diff();
    assertTrue(dif > -2 && dif < 2);
  }

  @Test
  void testRemove() {
    AVLSet<Long> tree = new AVLSet<>();
    for (int i = 0; i < (1 << 18); ++i) {
      tree.add(rnd.nextLong());
    }
    for (int i = 0; i < (1 << 18); ++i) {
      tree.remove(rnd.nextLong());
    }
    int dif = tree.root.diff();
    assertTrue(dif > -2 && dif < 2);
    tree.clear();
    for (long i = 0; i <= 100; ++i) {
      tree.add(i);
    }
    for (long i = 0; i < 100; i+=2) {
      tree.remove(i);
    }
    dif = tree.root.diff();
    assertTrue(dif > -2 && dif < 2);
  }

  @Test
  void size() {
    int sizeUniq = rnd.nextInt(10000);
    Long[] toCheckUniq = new Long[sizeUniq];
    AVLSet<Long> tree = new AVLSet<>();
    for (int i = 0; i < sizeUniq; ++i) {
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
      tree.add(tmp);
      toCheckUniq[i] = tmp;
    }
    for (int i = 0; i < sizeUniq; i+=2) {
      tree.remove(toCheckUniq[i]);
      tree.remove(toCheckUniq[i]);
    }
    assertEquals(tree.size(), sizeUniq / 2);
  }

  @Test
  void getAt() {
    int sizeUniq = rnd.nextInt(1000);
    Long[] toCheckUniq = new Long[sizeUniq];
    AVLSet<Long> tree = new AVLSet<>();
    for (int i = 0; i < sizeUniq; ++i) {
      Long tmp = rnd.nextLong(-1000, 1000);
      boolean flag = true;
      while (flag) {
        flag = false;
        for (Long toCheck : toCheckUniq) {
          if (toCheck == null) {
            continue;
          }
          if (toCheck == tmp) {
            flag = true;
            System.out.println("TRUE " + tmp);
            break;
          }
        }
        tmp = rnd.nextLong(-1000, 1000);
      }
      tree.add(tmp);
      toCheckUniq[i] = tmp;
    }
    Arrays.sort(toCheckUniq);
    for (Long tmp : toCheckUniq) {
      System.out.print(tmp + " ");
    }
    System.out.println();
    System.out.println(tree.root.order + " " + tree.root.getValue() + " " + sizeUniq + " " + tree.size());
    for (int i = 1; i <= sizeUniq; ++i) {
      System.out.println(i + " " + toCheckUniq[i - 1] + " " + tree.getAt(i));
      assertEquals(tree.getAt(i), toCheckUniq[i - 1]); // todo later: Test throws assert while getAt() function correct
    }
    for (int i = sizeUniq + 1; i <= 2 * sizeUniq; ++i) {
      assertNull(tree.getAt(i));
      assertNull(tree.getAt(-i));
    }
  }
}