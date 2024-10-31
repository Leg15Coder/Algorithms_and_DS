package utils;

public class Numbers {
  public static int nextTwoDegree(int number) {
    int cur = 1;
    while (((cur - 1) & number) != number) {
      cur <<= 1;
    }
    return cur;
  }
}
