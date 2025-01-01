package structures.arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class ArrayListTest extends ArrayTest {
  @BeforeEach
  void beforeEach() {
    arr = new ArrayList<>();
    size = rnd.nextInt(10000);
  }

  @AfterEach
  void afterEach() {
    arr.clear();
  }
}