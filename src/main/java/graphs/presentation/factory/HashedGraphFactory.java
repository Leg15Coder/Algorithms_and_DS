package graphs.presentation.factory;

import graphs.presentation.DirectedEdge;
import graphs.presentation.Edge;
import graphs.presentation.Vertex;

import java.util.HashMap;
import java.util.Map;

public class HashedGraphFactory<V extends Vertex, E extends Edge> implements GraphFactory<V, E> {
  private final Map<Integer, V> vertexes = new HashMap<>();
  private final Map<V, Map<V, E>> edges = new HashMap<>();

  @Override
  public void saveVertex(V vertex) {
    vertexes.put(vertex.hashCode(), vertex);
  }

  @Override
  public void saveEdge(E edge) {
    edges.put((V) edge.from(), edges.getOrDefault(edge.from(), new HashMap<>()));
    edges.get(edge.from()).put((V) edge.to(), edge);
  }

  @Override
  public V createVertex(Object state) {  // todo
    int hash = (int) state;
    return vertexes.get(hash);
  }

  @Override
  public E createEdge(V from, V to) {
    if (!edges.containsKey(from) || !edges.get(from).containsKey(to)) {
      saveEdge((E) new DirectedEdge(from, to));
    }

    return edges.get(from).get(to);
  }
}
