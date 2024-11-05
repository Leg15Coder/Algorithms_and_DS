package sortings;

import structures.basic.Queue;

class Sortings {
  public static long MergeSort(long[] array) {
    return MergeSortWithCount(array, 0, array.length - 1);
  }

  public static long MergeSort(int[] array) {
    return MergeSortWithCount(array, 0, array.length - 1);
  }

  private static long MergeSortWithCount(long[] array, int left, int right) {
    long count = 0;
    if (left < right) {
      int middle = (right + left) / 2;
      count += MergeSortWithCount(array, left, middle);
      count += MergeSortWithCount(array, middle + 1, right);
      count += merge(array, left, middle, right);
    }
    return count;
  }

  private static long MergeSortWithCount(int[] array, int left, int right) {
    long count = 0;
    if (left < right) {
      int middle = (right + left) / 2;
      count += MergeSortWithCount(array, left, middle);
      count += MergeSortWithCount(array, middle + 1, right);
      count += merge(array, left, middle, right);
    }
    return count;
  }

  private static long merge(long[] array, int left, int middle, int right) {
    long[] leftArray = new long[middle - left + 1];
    long[] rightArray = new long[right - middle];

    System.arraycopy(array, left, leftArray, 0, leftArray.length);
    System.arraycopy(array, middle + 1, rightArray, 0, rightArray.length);

    int i = 0, j = 0, k = left;
    long count = 0;

    while (i < leftArray.length && j < rightArray.length) {
      if (leftArray[i] <= rightArray[j]) {
        array[k++] = leftArray[i++];
      } else {
        array[k++] = rightArray[j++];
        count += middle + 1L - left - i;
      }
    }
    while (i < leftArray.length) {
      array[k++] = leftArray[i++];
    }
    while (j < rightArray.length) {
      array[k++] = rightArray[j++];
    }
    return count;
  }

  private static long merge(int[] array, int left, int middle, int right) {
    int[] leftArray = new int[middle - left + 1];
    int[] rightArray = new int[right - middle];

    System.arraycopy(array, left, leftArray, 0, leftArray.length);
    System.arraycopy(array, middle + 1, rightArray, 0, rightArray.length);

    int i = 0, j = 0, k = left;
    long count = 0;

    while (i < leftArray.length && j < rightArray.length) {
      if (leftArray[i] <= rightArray[j]) {
        array[k++] = leftArray[i++];
      } else {
        array[k++] = rightArray[j++];
        count += middle + 1L - left - i;
      }
    }
    while (i < leftArray.length) {
      array[k++] = leftArray[i++];
    }
    while (j < rightArray.length) {
      array[k++] = rightArray[j++];
    }
    return count;
  }

  public static void RadixSort(long[] array) {
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

  public static void RadixSort(int[] array) {
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

  public static void RadixSort(short[] array) {
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

  public static <T extends Comparable<T>> void bubbleSort(T[] arr) {
    for (int i = 0; i < arr.length; i++) {
      for (int j = i + 1; j < arr.length; j++) {
        if (arr[i].compareTo(arr[j]) > 0) {
          T temp = arr[i];
          arr[i] = arr[j];
          arr[j] = temp;
        }
      }
    }
  }


  public static void QuickSort() {

  }
}
