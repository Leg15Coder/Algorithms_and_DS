package sortings;

import structures.basic.Queue;

public class Sortings {
  public static <T extends Comparable<T>> long mergeSort(T[] array) { // todo JUnit test for count
    return mergeSortWithCount(array, 0, array.length - 1);
  }

  private static <T extends Comparable<T>> long mergeSortWithCount(T[] array, int left, int right) {
    long count = 0;
    if (left < right) {
      int middle = (right + left) / 2;
      count += mergeSortWithCount(array, left, middle);
      count += mergeSortWithCount(array, middle + 1, right);
      count += merge(array, left, middle, right);
    }
    return count;
  }

  private static <T extends Comparable<T>> long merge(T[] array, int left, int middle, int right) {
    Object[] leftArray = new Object[middle - left + 1];
    Object[] rightArray = new Object[right - middle];

    System.arraycopy(array, left, leftArray, 0, leftArray.length);
    System.arraycopy(array, middle + 1, rightArray, 0, rightArray.length);

    int i = 0, j = 0, k = left;
    long count = 0;

    while (i < leftArray.length && j < rightArray.length) {
      if (((T) leftArray[i]).compareTo((T) rightArray[j]) <= 0) {
        array[k++] = (T) leftArray[i++];
      } else {
        array[k++] = (T) rightArray[j++];
        count += middle + 1L - left - i;
      }
    }
    while (i < leftArray.length) {
      array[k++] = (T) leftArray[i++];
    }
    while (j < rightArray.length) {
      array[k++] = (T) rightArray[j++];
    }
    return count;
  }

  public static void radixSort(long[] array) {
    Queue<Long>[] queues = new Queue[256];
    for (int i = 0; i < queues.length; i++) {
      queues[i] = new Queue<Long>();
    }
    for (int k = 0; k < 8; k++) {
      for (long element : array) {
        int place = (int) ((element >> (8 * k)) & 255);
        queues[place].pushFront(element);
      }
      int j = 0;
      for (Queue<Long> queue : queues) {
        while (!queue.isEmpty()) {
          array[j] = queue.popBack();
          ++j;
        }
      }
    }
  }

  public static void radixSort(int[] array) {
    Queue<Integer>[] queues = new Queue[256];
    for (int i = 0; i < queues.length; i++) {
      queues[i] = new Queue<Integer>();
    }
    for (int k = 0; k < 4; k++) {
      for (int element : array) {
        int place = (element >> (8 * k)) & 255;
        queues[place].pushFront(element);
      }
      int j = 0;
      for (Queue<Integer> queue : queues) {
        while (!queue.isEmpty()) {
          array[j] = queue.popBack();
          ++j;
        }
      }
    }
  }

  public static void radixSort(short[] array) {
    Queue<Short>[] queues = new Queue[256];
    for (int i = 0; i < queues.length; i++) {
      queues[i] = new Queue<Short>();
    }
    for (int k = 0; k < 2; k++) {
      for (short element : array) {
        int place = (int) ((element >> (8 * k)) & 255);
        queues[place].pushFront(element);
      }
      int j = 0;
      for (Queue<Short> queue : queues) {
        while (!queue.isEmpty()) {
          array[j] = queue.popBack();
          ++j;
        }
      }
    }
  }

  public static void radixSort(byte[] array) {
    Queue<Byte>[] queues = new Queue[256];
    for (int i = 0; i < queues.length; i++) {
      queues[i] = new Queue<>();
    }
    for (int k = 0; k < 1; k++) {
      for (byte element : array) {
        int place = (int) (element & 255);
        queues[place].pushFront(element);
      }
      int j = 0;
      for (Queue<Byte> queue : queues) {
        while (!queue.isEmpty()) {
          array[j] = queue.popBack();
          ++j;
        }
      }
    }
  }

  public static <T extends Comparable<T>> void bubbleSort(T[] array) {
    for (int i = 0; i < array.length; i++) {
      for (int j = i + 1; j < array.length; j++) {
        if (array[i].compareTo(array[j]) > 0) {
          T temp = array[i];
          array[i] = array[j];
          array[j] = temp;
        }
      }
    }
  }

  public static <T extends Comparable<T>> void quickSort(T[] array) {
    quickSort(array, 0, array.length);
  }

  private static <T extends Comparable<T>> void quickSort(T[] array, int left, int right) {
    if (left < right) {
      int middle = partition(array, left, right);

      quickSort(array, left, middle);
      quickSort(array, middle, right);
    }
  }

  private static <T extends Comparable<T>> int partition(T[] array, int left, int right) {
    right--;
    T pivot = array[right];
    int index = left;

    for (int i = left; i < right; i++) {
      if (array[i].compareTo(pivot) <= 0) {
        T temp = array[index];
        array[index] = array[i];
        array[i] = temp;
        index++;
      }
    }

    T temp = array[index];
    array[index] = array[right];
    array[right] = temp;

    return index;
  }
}
