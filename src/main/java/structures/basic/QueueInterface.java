package structures.basic;

public interface QueueInterface<T> {
  T back();

  void pushFront(T element);

  T popBack();

  int getSize();

  boolean isEmpty();
}
