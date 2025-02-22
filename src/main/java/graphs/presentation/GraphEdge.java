package graphs.presentation;

public class GraphEdge implements Edge {
  private final GraphVertex first;
  private final GraphVertex second;

  public GraphEdge(GraphVertex first, GraphVertex second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public Edge reverse() {
    return new GraphEdge(second, first);
  }

  @Override
  public Vertex first() {
    return first;
  }

  @Override
  public Vertex second() {
    return second;
  }
}
