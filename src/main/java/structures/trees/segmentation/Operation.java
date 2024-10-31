package structures.trees.segmentation;

public interface Operation<T> {
  T func(T left, T right);

  T neutral();
}
