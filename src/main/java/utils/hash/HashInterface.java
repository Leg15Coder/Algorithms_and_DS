package utils.hash;

public interface HashInterface<T> {
  Integer hash(T obj);

  boolean isInCollision(T left, T right);

  Integer minValue();

  Integer maxValue();
}
