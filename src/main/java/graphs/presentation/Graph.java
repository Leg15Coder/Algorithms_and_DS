package graphs.presentation;

public interface Graph<V extends Vertex, E extends Edge<V>> {
  void addEdge(E edge);

  void removeEdge(E edge);

  boolean isEdge(E edge);

  void addVertex(V vertex);

  void removeVertex(V vertex);

  boolean isVertexExists(V vertex);

  int neighboursCount(V vertex);

  Iterable<V> neighbours(V vertex);

  Iterable<E> adjacentEdges(V vertex);

  Iterable<V> getAllVertexes();

  Iterable<E> getAllEdges();

  Graph<V, E> transpose();

  Graph<V, E> createSubGraph(Iterable<V> subVertexes);

  int vertexCount();

  int edgesCount();

  boolean isDirected();

  void clear();
}
