package graphs.presentation;

public interface Graph<V extends Vertex, E extends Edge> {
  void addEdge(E edge);

  void removeEdge(E edge);

  boolean isEdge(E edge);

  void addVertex(V vertex);

  void removeVertex(V vertex);

  boolean isVertexExists(V vertex);

  int neighboursCount(V vertex);

  Iterable<V> neighbours(V vertex);

  Iterable<E> adjacentEdges(V vertex);

  V getAnyUnusedVertex(Iterable<V> usedVertexes);

  E getAnyUnusedEdge(Iterable<E> usedEdges);

  Iterable<V> getAllUnusedVertex(Iterable<V> usedVertexes);

  Iterable<E> getAllUnusedEdge(Iterable<E> usedEdges);

  Iterable<V> getAllVertexes();

  Iterable<E> getAllEdges();

  Graph<V, E> transpose();

  Graph<V, E> createSubGraph(Iterable<V> subVertexes);

  int vertexCount();

  int edgesCount();

  void clear();
}
