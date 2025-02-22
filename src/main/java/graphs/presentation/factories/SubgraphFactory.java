package graphs.presentation.factories;

import graphs.presentation.*;

import java.util.HashMap;
import java.util.Map;

public class SubgraphFactory<V extends Vertex, E extends Edge> implements GraphComponentsFactory<GraphVertex<V, E>, GraphEdge> {
  private final Map<Integer, Graph<V, E>> memory = new HashMap<>();

  @Override
  public GraphVertex<V, E> createVertex(int index) {
    return new GraphVertex<>(index, memory.getOrDefault(index, null));
  }

  @Override
  public GraphEdge createEdge(GraphVertex<V, E> start, GraphVertex<V, E> end) {
    return new GraphEdge(start, end);
  }

  public GraphVertex<V, E> save(int index, Graph<V, E> graph) {
    this.memory.put(index, graph);
    return createVertex(index);
  }
}
