package structures.trees.search;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SplayTreeTest {

  Random rnd = new Random();

  @Test
  void add() {
    SplayTree<Long> tree = new SplayTree<>();
    tree.add(rnd.nextLong(), rnd.nextLong());
    assertNotNull(tree.root);
  }

  @Test
  void remove() {
    int sizeUniq = rnd.nextInt(1000);
    HashMap<Long, Long> map = new HashMap<>();
    SplayTree<Long> tree = new SplayTree<>();
    for (int i = 0; i < sizeUniq; ++i) {
      Long tmp = rnd.nextLong();
      boolean flag = true;
      while (flag) {
        flag = false;
        for (Long toCheck : map.keySet()) {
          if (Objects.equals(toCheck, tmp)) {
            flag = true;
            break;
          }
        }
        tmp = rnd.nextLong();
      }
      Long val = rnd.nextLong();
      tree.add(tmp, val);
      map.put(tmp, val);
    }
    for (Long toCheck : map.keySet()) {
      assertTrue(tree.remove(toCheck));
      assertFalse(tree.remove(toCheck));
    }
  }

  @Test
  void get() {
    int sizeUniq = rnd.nextInt(1000);
    HashMap<Long, Long> map = new HashMap<>();
    SplayTree<Long> tree = new SplayTree<>();
    for (int i = 0; i < sizeUniq; ++i) {
      Long tmp = rnd.nextLong();
      boolean flag = true;
      while (flag) {
        flag = false;
        for (Long toCheck : map.keySet()) {
          if (Objects.equals(toCheck, tmp)) {
            flag = true;
            break;
          }
        }
        tmp = rnd.nextLong();
      }
      Long val = rnd.nextLong();
      tree.add(tmp, val);
      map.put(tmp, val);
    }
    for (Long toCheck : map.keySet()) {
      assertEquals(tree.getAt(toCheck), map.get(toCheck));
    }
  }
}