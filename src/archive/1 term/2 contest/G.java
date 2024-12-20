import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
  private static final int COUNT_OF_BYTES_IN_LONG = 8;
  private static final int COUNT_OF_BITS_IN_BYTE = 8;
  private static final int COUNT_OF_VALUES_IN_BYTE = 256;

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    int amount = input.nextInt();
    ArrayList<Long> array = new ArrayList<>();
    for (int i = 0; i < amount; i++) {
      array.add(input.nextLong());
    }
    radixSort(array);
    for (Long element : array) {
      System.out.println(element);
    }
  }

  public static void radixSort(ArrayList<Long> array) {
    ArrayList<ArrayDeque<Long>> queues = new ArrayList<>();
    for (int i = 0; i < COUNT_OF_VALUES_IN_BYTE; i++) {
      queues.add(new ArrayDeque<Long>());
    }
    for (int k = 0; k < COUNT_OF_BYTES_IN_LONG; k++) {
      for (int i = 0; i < array.size(); i++) {
        int place = (int) ((array.get(i) >> (COUNT_OF_BITS_IN_BYTE * k)) & (COUNT_OF_VALUES_IN_BYTE - 1));
        queues.get(place).addLast(array.get(i));
      }
      int j = 0;
      for (int i = 0; i < queues.size(); i++) {
        while (!queues.get(i).isEmpty()) {
          array.set(j, queues.get(i).removeFirst());
          j++;
        }
      }
    }
  }
}