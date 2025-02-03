package graphs.presentation;

import graphs.exceptions.EdgeAlreadyExistsException;
import graphs.exceptions.VertexIndexOutOfRangeException;

import java.util.List;

public interface Graph<V extends Vertex, E extends Edge> {
  void addEdge(E edge) throws EdgeAlreadyExistsException;

  void removeEdge(E edge) throws EdgeAlreadyExistsException;

  boolean isEdge(E edge);

  int neighboursCount(V vertex);

  List<V> neighbours(V vertex);

  V getVertexByIndex(int index) throws VertexIndexOutOfRangeException;

  boolean isVertexIndexExists(int index);

  V getAnyUnusedVertex(Iterable<V> usedVertexes);
}
