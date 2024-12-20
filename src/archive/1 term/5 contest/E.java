import java.util.ArrayList;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    int n = input.nextInt();
    long[] a = new long[n];

    for (int i = 0; i < n; ++i) {
      a[i] = input.nextLong();
    }

    ArrayList<Long> arr1 = new ArrayList<>();
    ArrayList<Long> arr2 = new ArrayList<>();
    arr1.add(a[0]);
    arr2.add(a[0]);

    boolean flag = false;
    long last = a[0];
    for (int i = 1; i < n; ++i) {
      if (a[i] == last) {
        continue;
      }
      if (flag ^ (a[i] > last)) {
        arr1.remove(arr1.size() - 1);
        arr1.add(a[i]);
      } else {
        flag = !flag;
        arr1.add(a[i]);
      }
      last = a[i];
    }

    flag = true;
    last = a[0];
    for (int i = 1; i < n; ++i) {
      if (a[i] == last) {
        continue;
      }
      if (flag ^ (a[i] > last)) {
        arr2.remove(arr2.size() - 1);
        arr2.add(a[i]);
      } else {
        flag = !flag;
        arr2.add(a[i]);
      }
      last = a[i];
    }

    if (arr1.size() < arr2.size()) {
      arr1 = arr2;
    }

    System.out.println(arr1.size());
    for (var e : arr1) {
      System.out.print(e + " ");
    }
  }
}
