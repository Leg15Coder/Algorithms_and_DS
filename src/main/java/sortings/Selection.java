package sortings;

import static utils.Compare.min;
import static utils.Swap.swap;

public class Selection {
  public static <T extends Comparable<T>> T quickSelect(T[] array, int order) {
    return quickSelect(array, order, 0, array.length);
  }

  public static <T extends Comparable<T>> T quickSelect(T[] array, int order, int left, int right) {
    if (right < left) {
      return null;  // todo exception
    }
    if (right == left) {
      return array[left];
    }

    T pivot = medianOfMedians(array, left, right);
    int index = partition(array, pivot, left, right);

    if (index > order) {
      return quickSelect(array, order, left, index);
    } else if (index < order) {
      return quickSelect(array, order, index + 1, right);
    } else {
      return array[index];  // todo. Where mistake while partition without fat blocks
    }
  }

  public static <T extends Comparable<T>> int partition(T[] array, T pivot, int left, int right) {  // todo fat partition: add block of equals values
    int index = left;

    for (int i = left; i < right; i++) {
      if (array[i].compareTo(pivot) <= 0) {
        swap(array, index++, i);
      }
    }

    return index;
  }

  private static <T extends Comparable<T>> T medianOfMedians(T[] array, int left, int right) {
    int index = left;

    for (int i = left; i < right; i+=5) {
      int median = medianOfFive(array, i);
      swap(array, median, index++);
    }

    return quickSelect(array, (index + left) / 2, left, index);
  }

  private static <T extends Comparable<T>> int medianOfFive(T[] array, int index) {
    int roof = min(index + 5, array.length);

    for (int i = index; i < roof; i++) {
      for (int j = i; j > index; j--) {
        if (array[j - 1].compareTo(array[j]) < 0) {
          swap(array, j, j - 1);
        }
      }
    }

    return (index + roof) / 2;
  }
}
