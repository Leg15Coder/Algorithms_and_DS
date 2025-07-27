package graphs.presentation;

public record IndexedVertex(int index) implements Vertex {
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof IndexedVertex that)) return false;
    return index == that.index;
  }

  @Override
  public int hashCode() {
    return index;
  }
}
