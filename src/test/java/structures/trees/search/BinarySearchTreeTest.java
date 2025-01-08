package structures.trees.search;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class BinarySearchTreeTest extends SearchTreeTest {
  @BeforeEach
  void init() {
    this.tree = new BinarySearchTree<>();
    this.size = rnd.nextInt(1,1000);
  }

  @AfterEach
  void end() {
    this.tree.clear();
  }
}