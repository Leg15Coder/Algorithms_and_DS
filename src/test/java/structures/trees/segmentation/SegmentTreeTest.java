package structures.trees.segmentation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import structures.basic.ArrayList;
import structures.trees.segmentation.funcs.Sum;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SegmentTreeTest {
  Random rnd = new Random();
  SegmentTree<Long> tree;
  int size;
  Range seg;

  @BeforeEach
  void init() {
    this.size = rnd.nextInt(1, 100000);
    ArrayList<Long> arr = new ArrayList<>();
    for (int i = 0; i < size; ++i) {
      arr.add(rnd.nextLong());
    }
    this.tree = new SegmentTree<>(arr, new Sum(), new Sum()); // todo
  }

  @Test
  void get() {
  }

  @Test
  void testGet() {
  }

  @Test
  void testGet1() {
  }

  @Test
  void set() {
  }

  @Test
  void updateSegmentWith() {
  }

  @Test
  void testUpdateSegmentWith() {
  }

  @Test
  void setOnSegment() {
  }

  @Test
  void testSetOnSegment() {
  }

  @Test
  void clearSegment() {
  }

  @Test
  void testClearSegment() {
  }

  @Test
  void clear() {
  }

  @Test
  void getSize() {
  }
}