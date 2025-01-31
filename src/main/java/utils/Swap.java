package utils;

public class Swap {
  public static <T> void swap(T[] array, int firstIndex, int secondIndex) {
    T temp = array[firstIndex];
    array[firstIndex] = array[secondIndex];
    array[secondIndex] = temp;
  }
}
