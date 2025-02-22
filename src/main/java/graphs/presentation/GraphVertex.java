package graphs.presentation;

import java.util.Objects;

public record GraphVertex<V extends Vertex, E extends Edge>(int index, Graph<V, E> graph) implements Vertex, Comparable<Vertex> {
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof GraphVertex that)) return false;
    return index == that.index;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(index);
  }

  @Override
  public int compareTo(Vertex o) {
    return Integer.compare(index, o.index());
  }
}
