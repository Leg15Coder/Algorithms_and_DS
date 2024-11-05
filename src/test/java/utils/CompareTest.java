package utils;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

import utils.Compare;

class CompareTest {

  static Random rnd = new Random();

  @Test
  void maxTest() {
    Long first = rnd.nextLong();
    Long second = rnd.nextLong();
    Long result = java.lang.Long.max(first, second);
    assertEquals(Compare.max(first, second), result);
    assertEquals(Compare.max(null, second), second);
    assertEquals(Compare.max(first, null), first);
    assertNull(Compare.max(null, null));
  }

  @Test
  void minTest() {
    Long first = rnd.nextLong();
    Long second = rnd.nextLong();
    Long result = java.lang.Long.min(first, second);
    assertEquals(Compare.min(first, second), result);
    assertEquals(Compare.min(null, second), second);
    assertEquals(Compare.min(first, null), first);
    assertNull(Compare.min(null, null));
  }
}