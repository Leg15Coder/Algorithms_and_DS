import java.util.Scanner;
import java.util.TreeSet;

public class Main {
  static long hash(String s) {
    int base = 256;
    int count = 0;
    long mod = 1000000007;
    long h = 0;
    int[] letters = new int[base];

    for (int i = 0; i < s.length(); ++i) {
      int symb = s.charAt(i) - 'a';

      int x;
      if (letters[symb] != 0) {
        x = letters[symb];
      } else {
        letters[symb] = ++count;
        x = count;
      }

      h *= base;
      h += x;
      h %= mod;
    }

    return h;
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    int n = input.nextInt();

    TreeSet<Long> hashes = new TreeSet<>();

    for (int i = 0; i < n; ++i) {
      String command = input.next();
      String s = input.next();
      long h = hash(s);

      if (command.equals("+")) {
        hashes.add(h);
      } else {
        if (hashes.contains(h)) {
          System.out.println("YES");
        } else {
          System.out.println("NO");
        }
      }
    }
  }
}
