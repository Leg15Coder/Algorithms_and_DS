package utils;

public class Compare {
  public static <T extends Comparable<T>> T max(T l, T r) {
    if (l == null & r == null) {
      return null;
    }
    if (l == null) {
      return r;
    }
    if (r == null) {
      return l;
    }
    return (l.compareTo(r) > 0) ? l : r;
  }

  public static <T extends Comparable<T>> T min(T l, T r) {
    if (l == null & r == null) {
      return null;
    }
    if (l == null) {
      return r;
    }
    if (r == null) {
      return l;
    }
    return (l.compareTo(r) > 0) ? r : l;
  }
}