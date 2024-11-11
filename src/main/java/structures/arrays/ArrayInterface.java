package structures.arrays;

public interface ArrayInterface<T> {
  T getAt(int index);

  void add(T value);

  T pop();

  boolean remove(T value);

  void remove(int index);

  void insert(T value, int index);

  void insert(T value);

  int getSize();

  void clear();
}
