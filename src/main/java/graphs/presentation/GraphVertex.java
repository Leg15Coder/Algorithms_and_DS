package graphs.presentation;

public record GraphVertex<V extends Vertex, E extends Edge>(Graph<V, E> graph) implements Vertex {
  @Override
  public int compareTo(Vertex o) {
    return Integer.compare(hashCode(), o.hashCode());
  }
}
