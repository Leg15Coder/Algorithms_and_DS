package graphs.presentation;

import java.util.Objects;

public record SimpleEdge(Vertex first, Vertex second) implements Edge {
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
    if (!(o instanceof SimpleEdge that)) return false;
    return Objects.equals(first, that.first) && Objects.equals(second, that.second);
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second);
  }
}
