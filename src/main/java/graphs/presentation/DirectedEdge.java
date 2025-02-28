package graphs.presentation;

public record DirectedEdge(Vertex from, Vertex to) implements Edge {
  @Override
  public Vertex from() {
    return from;
  }

  @Override
  public Vertex to() {
    return to;
  }
}
