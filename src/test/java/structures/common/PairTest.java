package structures.common;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PairTest {

  @Test
  public void justCreate() {
    Pair<String, Long> pair = new Pair<>("hello", 10L);
    assertEquals(pair.first, "hello");
    assertEquals(pair.second, 10L);
  }
}