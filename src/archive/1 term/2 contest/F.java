import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  static final int MOD = 10004321;

  public static <T extends Comparable<T>> T quickSelect(List<T> array, int order) {
    --order;

    int left = 0;
    int right = array.size();
    int currentOrder = partition(array, left, right);

    while (currentOrder != order) {
      if (currentOrder < order) {
        left = currentOrder + 1;
      } else {
        right = currentOrder;
      }

      currentOrder = partition(array, left, right);
    }

    return array.get(currentOrder);
  }

  private static <T extends Comparable<T>> int partition(List<T> array, int left, int right) {
    --right;
    T pivot = array.get(right);
    int index = left;

    for (int i = left; i < right; ++i) {
      if (array.get(i).compareTo(pivot) <= 0) {
        T temp = array.get(index);
        array.set(index, array.get(i));
        array.set(i, temp);
        index++;
      }
    }

    T temp = array.get(index);
    array.set(index, array.get(right));
    array.set(right, temp);

    return index;
  }

  public static class Generator {
    private final List<Integer> array;
    private int next;

    public Generator(List<Integer> array, int first, int second) {
      this.array = array;
      array.add(first);
      array.add(second);
      this.next = second;
    }

    public int getFirst() {
      return array.get(0);
    }

    public void generateNext() {
      this.next = (123 * array.get(array.size() - 1) + 45 * array.get(array.size() - 2)) % MOD;
      array.add(next);
    }

    public void generateTo(int value) {
      for (int i = array.size(); i < value; ++i) {
        generateNext();
      }
    }
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    int countOfElements = input.nextInt();
    int orderStatistic = input.nextInt();
    int first = input.nextInt();
    int second = input.nextInt();

    ArrayList<Integer> array = new ArrayList<>();

    if (countOfElements > 1) {
      Generator generator = new Generator(array, first, second);
      generator.generateTo(countOfElements);
    } else {
      array.add(first);
    }

    System.out.println(quickSelect(array, orderStatistic));
  }
}
