package graphs.presentation;

import java.util.Objects;

public record IndexedVertex(int index) implements Vertex {
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof IndexedVertex that)) return false;
    return index == that.index;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(index);
  }

  @Override
  public String toString() {
    return index + "";
  }

  @Override
  public int compareTo(Vertex o) {
    return Integer.compare(hashCode(), o.hashCode());
  }
}
