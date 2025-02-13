package graphs.presentation;

import java.util.Objects;

public record BasicEdge(Vertex first, Vertex second) implements Edge {
  @Override
  public Vertex first() {
    return first;
  }

  @Override
  public Vertex second() {
    return second;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof BasicEdge that)) return false;
    return Objects.equals(first, that.first) && Objects.equals(second, that.second);
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second);
  }

  @Override
  public Edge reverse() {
    return new BasicEdge(second, first);
  }
}
