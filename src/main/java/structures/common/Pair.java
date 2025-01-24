package structures.common;

import java.util.Objects;

public class Pair<T, E> {
  public T first;
  public E second;

  public Pair(T first, E second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Pair<?, ?> pair)) return false;
    return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second);
  }
}
