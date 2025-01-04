package structures.trees.search;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class SplayTest extends SearchTreeTest {
  @BeforeEach
  void init() {
    this.tree = new SplayTree<>();
    this.size = rnd.nextInt(1,10000);
  }

  @AfterEach
  void end() {
    this.tree.clear();
  }
}