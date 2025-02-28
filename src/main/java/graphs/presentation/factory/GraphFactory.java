package graphs.presentation.factory;

import graphs.presentation.Edge;
import graphs.presentation.Vertex;

public interface GraphFactory<V extends Vertex, E extends Edge> {
  void saveVertex(V vertex);

  void saveEdge(E edge);

  V createVertex(V vertex);

  E createEdge(V from, V to);
}
