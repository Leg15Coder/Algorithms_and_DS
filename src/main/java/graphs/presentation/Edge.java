package graphs.presentation;

public interface Edge<V extends Vertex> {
  V from();

  V to();

  Edge<V> reverseArguments();
}
