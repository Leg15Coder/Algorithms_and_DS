package structures.arrays;

public interface ArrayInterface<T> {
  T getAt(int index);

  T first();

  T last();

  void setAt(int index, T value);

  int getIndex(T value);

  T pop();

  boolean remove(T value);

  void remove(int index);

  void insert(T value, int index);

  void insert(T value);

  int getSize();

  void clear();

  boolean isEmpty();
}
