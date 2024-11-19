package utils.hash;

public interface HashInterface<T> {
  Long hash(T obj);

  boolean isInCollision(T left, T right);
}
