package graphs.presentation;

import java.util.Objects;

public record BasicVertex(int index) implements Vertex {
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof BasicVertex that)) return false;
    return index == that.index;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(index);
  }
}
