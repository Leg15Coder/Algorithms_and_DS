package graphs.presentation;

import java.util.List;

public interface Graph<V extends Vertex, E extends Edge> {
  void addEdge(E edge);

  void removeEdge(E edge);

  boolean isEdge(E edge);

  int neighboursCount(V vertex);

  List<V> neighbours(V vertex);

  V getVertexByIndex(int index);

  boolean isVertexIndexExists(int index);

  int getColor(V vertex);

  void setColor(V vertex, int color);

  void clearColors();

  V getAnyUnusedVertex(Iterable<V> usedVertexes);

  int size();

  void clear();

  Graph<V, E> transpose();
}
