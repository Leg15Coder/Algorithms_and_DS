package structures.arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


class LinkedArrayTest extends ArrayTest {
  @BeforeEach
  void beforeEach() {
    arr = new LinkedArray<>();
    size = rnd.nextInt(10000);
  }

  @AfterEach
  void afterEach() {
    arr.clear();
  }
}