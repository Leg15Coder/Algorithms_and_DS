package structures.common;

public class Pair<T, E> implements PairInterface<T, E> {
  public final T first;
  public final E second;

  public Pair(T first, E second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public T getFirst() {
    return first;
  }

  @Override
  public E getSecond() {
    return second;
  }
}
