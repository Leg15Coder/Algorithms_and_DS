package sortings;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static java.util.Arrays.sort;
import static org.junit.jupiter.api.Assertions.*;

class SortingsTest {

  static Random rnd = new Random();

  @Test
  void mergeSort() {
    int size = rnd.nextInt(50000);
    Long[] checker = new Long[size];
    Long[] result = new Long[size];
    for (int i = 0; i < size; ++i) {
      long tmp = rnd.nextLong();
      checker[i] = tmp;
      result[i] = tmp;
    }
    sort(checker);
    Sortings.mergeSort(result);
    for (int i = 0; i < size; ++i) {
      assertEquals(result[i], checker[i]);
    }
  }

  @Test
  void testRadixSortLong() {
    int size = rnd.nextInt(50000);
    long[] checker = new long[size];
    long[] result = new long[size];
    for (int i = 0; i < size; ++i) {
      long tmp = rnd.nextLong(0, 1000000000);
      checker[i] = tmp;
      result[i] = tmp;
    }
    sort(checker);
    Sortings.radixSort(result);
    for (int i = 0; i < size; ++i) {
      assertEquals(result[i], checker[i]);
    }
  }

  @Test
  void testRadixSortInteger() {
    int size = rnd.nextInt(50000);
    int[] checker = new int[size];
    int[] result = new int[size];
    for (int i = 0; i < size; ++i) {
      int tmp = rnd.nextInt(0, 1000000);
      checker[i] = tmp;
      result[i] = tmp;
    }
    sort(checker);
    Sortings.radixSort(result);
    for (int i = 0; i < size; ++i) {
      assertEquals(result[i], checker[i]);
    }
  }

  @Test
  void testRadixSortShort() {
    int size = rnd.nextInt(50000);
    short[] checker = new short[size];
    short[] result = new short[size];
    for (int i = 0; i < size; ++i) {
      short tmp = (short) rnd.nextInt(0, 32000);
      checker[i] = tmp;
      result[i] = tmp;
    }
    sort(checker);
    Sortings.radixSort(result);
    for (int i = 0; i < size; ++i) {
      assertEquals(result[i], checker[i]);
    }
  }

  @Test
  void testRadixSortByte() {
    int size = rnd.nextInt(50000);
    byte[] checker = new byte[size];
    byte[] result = new byte[size];
    for (int i = 0; i < size; ++i) {
      byte tmp = (byte) rnd.nextInt(0, 127);
      checker[i] = tmp;
      result[i] = tmp;
    }
    sort(checker);
    Sortings.radixSort(result);
    for (int i = 0; i < size; ++i) {
      assertEquals(result[i], checker[i]);
    }
  }

  @Test
  void bubbleSort() {
    int size = rnd.nextInt(10000);
    Long[] checker = new Long[size];
    Long[] result = new Long[size];
    for (int i = 0; i < size; ++i) {
      long tmp = rnd.nextLong();
      checker[i] = tmp;
      result[i] = tmp;
    }
    sort(checker);
    Sortings.bubbleSort(result);
    for (int i = 0; i < size; ++i) {
      assertEquals(result[i], checker[i]);
    }
  }

  @Test
  void quickSort() {
    int size = rnd.nextInt(10000);
    Long[] checker = new Long[size];
    Long[] result = new Long[size];
    for (int i = 0; i < size; ++i) {
      long tmp = rnd.nextLong();
      checker[i] = tmp;
      result[i] = tmp;
    }
    sort(checker);
    Sortings.bubbleSort(result);
    for (int i = 0; i < size; ++i) {
      assertEquals(result[i], checker[i]);
    }
  }
}