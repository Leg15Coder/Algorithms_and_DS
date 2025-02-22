package graphs.presentation.factories;

import graphs.presentation.Edge;
import graphs.presentation.Vertex;

public interface GraphComponentsFactory<V extends Vertex, E extends Edge> {
  V createVertex(int index);

  E createEdge(V start, V end);
}
