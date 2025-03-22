package graphs.presentation.factory;

import graphs.presentation.Edge;
import graphs.presentation.Vertex;

import java.util.HashMap;
import java.util.Map;

public class HashedGraphFactory<V extends Vertex, E extends Edge<V>> implements GraphFactory<V, E> {
  private final Map<Integer, V> vertexes = new HashMap<>();
  private final Map<V, Map<V, E>> edges = new HashMap<>();

  @Override
  public void saveVertex(V vertex) {
    vertexes.put(vertex.hashCode(), vertex);
  }

  @Override
  public void saveEdge(E edge) {
    edges.put(edge.from(), edges.getOrDefault(edge.from(), new HashMap<>()));
    edges.get(edge.from()).put(edge.to(), edge);
  }

  @Override
  public V createVertex(Object state) {
    return vertexes.get((int) state);
  }

  @Override
  public E createEdge(V from, V to) {
    if (!edges.containsKey(from) || !edges.get(from).containsKey(to)) {
      saveEdge((E) new BasicEdge(from, to));
    }

    return edges.get(from).get(to);
  }
}
