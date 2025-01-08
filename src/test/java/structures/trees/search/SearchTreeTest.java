package structures.trees.search;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import sortings.Sortings;
import structures.basic.Heap;

import java.util.Random;

public abstract class SearchTreeTest {
  Random rnd = new Random();
  BinarySearchTreeInterface<Long> tree;
  int size;

  @Test
  void add() {
    tree.add(rnd.nextLong());
    assertFalse(tree.isEmpty());
  }

  @Test
  void remove() {
    int sizeUnique = rnd.nextInt(1, size);
    long[] toCheck = new long[size];

    for (int i = 0; i < size; ++i) {
      long tmp = 0;

      boolean flag = true;
      while (flag) {
        flag = false;
        tmp = rnd.nextLong();
        for (var e : toCheck) {
          if (tmp == e) {
            flag = true;
            break;
          }
        }
      }

      toCheck[i] = tmp;
      if (i >= sizeUnique) {
        tree.add(tmp);
      }

      tree.add(tmp);
    }

    for (int i = 0; i < size; ++i) {
      var e = toCheck[i];
      assertTrue(tree.remove(e));

      if (i < sizeUnique) {
        assertFalse(tree.get(e));
      } else {
        assertTrue(tree.get(e));
      }
    }
  }

  @Test
  void delete() {
    int sizeUnique = rnd.nextInt(1, size);
    long[] toCheck = new long[size];

    for (int i = 0; i < size; ++i) {
      long tmp = 0;

      boolean flag = true;
      while (flag) {
        flag = false;
        tmp = rnd.nextLong();
        for (var e : toCheck) {
          if (tmp == e) {
            flag = true;
            break;
          }
        }
      }

      toCheck[i] = tmp;
      if (i >= sizeUnique) {
        tree.add(tmp);
      }

      tree.add(tmp);
    }

    for (int i = 0; i < size; ++i) {
      var e = toCheck[i];
      assertTrue(tree.get(e));
      tree.delete(e);
      assertFalse(tree.get(e));
    }

    assertTrue(tree.isEmpty());
  }

  @Test
  void get() {
    long[] toCheck = new long[size];

    for (int i = 0; i < size; ++i) {
      long tmp = 2 * rnd.nextLong();
      toCheck[i] = tmp;
      tree.add(tmp);
    }

    for (var e : toCheck) {
      assertTrue(tree.get(e));
      assertFalse(tree.get(e - 1));
      assertFalse(tree.get(e + 1));
    }
  }


  @Test
  void getMin() {
    Heap<Long> heap = new Heap<>();
    long[] toCheck = new long[size];

    for (int i = 0; i < size; ++i) {
      long tmp = rnd.nextLong();
      toCheck[i] = tmp;
      heap.insert(tmp);
      tree.add(tmp);
    }

    for (var e : toCheck) {
      assertEquals(heap.root(), tree.getMin());

      if (e == heap.root()) {
        tree.remove(e);
        heap.extract();
      }
    }
  }

  @Test
  void getMax() {
    Heap<Long> heap = new Heap<>(false);
    long[] toCheck = new long[size];

    for (int i = 0; i < size; ++i) {
      long tmp = rnd.nextLong();
      toCheck[i] = tmp;
      heap.insert(tmp);
      tree.add(tmp);
    }

    for (var e : toCheck) {
      assertEquals(heap.root(), tree.getMax());

      if (e == heap.root()) {
        tree.remove(e);
        heap.extract();
      }
    }
  }

  @Test
  void next() {
    Long[] toCheck = new Long[size];

    for (int i = 0; i < size; ++i) {
      long tmp = 2 * rnd.nextLong();
      toCheck[i] = tmp;
      tree.add(tmp);
    }

    Sortings.mergeSort(toCheck);

    for (int i = 0; i < size; ++i) {
      assertEquals(toCheck[i], tree.next(toCheck[i]));
      if (i < size - 1) {
        assertEquals(toCheck[i], tree.next(toCheck[i] - 1));
      } else {
        assertNull(tree.next(toCheck[i] + 1));
      }
    }
  }

  @Test
  void previous() {
    Long[] toCheck = new Long[size];

    for (int i = 0; i < size; ++i) {
      long tmp = 2 * rnd.nextLong();
      toCheck[i] = tmp;
      tree.add(tmp);
    }

    Sortings.mergeSort(toCheck);

    for (int i = 0; i < size; ++i) {
      assertEquals(toCheck[i], tree.previous(toCheck[i]));
      if (i > 0) {
        assertEquals(toCheck[i], tree.previous(toCheck[i] + 1));
      } else {
        assertNull(tree.previous(toCheck[i] - 1));
      }
    }
  }

  @Test
  void isEmpty() {
    for (int i = 0; i < size; ++i) {
      tree.add(rnd.nextLong());
      assertFalse(tree.isEmpty());
    }

    tree.clear();
    assertTrue(tree.isEmpty());
  }

  @Test
  void size() {
    long[] toCheck = new long[size];
    int copySize = size;

    for (int i = 0; i < size; ++i) {
      long tmp = rnd.nextLong();
      toCheck[i] = tmp;
      tree.add(tmp);
    }

    assertEquals(copySize, tree.getSize());

    for (var e : toCheck) {
      tree.remove(e);
      assertEquals(--copySize, tree.getSize());
    }

    tree.clear();
    assertEquals(0, tree.getSize());
    int sizeUnique = rnd.nextInt(size);
    int realSize = 0;

    for (int i = 0; i < size; ++i) {
      long tmp = 0;

      boolean flag = true;
      while (flag) {
        flag = false;
        tmp = rnd.nextLong();
        for (var e : toCheck) {
          if (tmp == e) {
            flag = true;
            break;
          }
        }
      }

      toCheck[i] = tmp;
      for (int j = 0; j <= i - sizeUnique; j+=10) {
        tree.add(tmp);
        ++realSize;
      }

      tree.add(tmp);
      ++realSize;
    }

    assertEquals(realSize, tree.getSize());

    for (int i = 0; i < size; ++i) {
      tree.delete(toCheck[i]);
      if (i < sizeUnique) {
        assertEquals(--realSize, tree.getSize());
      } else {
        realSize -= (i - sizeUnique) / 10 + 2;
        assertEquals(realSize, tree.getSize());
      }
    }
  }
}
