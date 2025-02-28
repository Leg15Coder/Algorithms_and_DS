package graphs.traversals;

import graphs.exceptions.CycleDetectedException;
import graphs.presentation.Edge;
import graphs.presentation.Graph;
import graphs.presentation.Vertex;
import graphs.presentation.visitors.Visitor;
import structures.common.Pair;

import java.util.ArrayList;
import java.util.List;

import static graphs.traversals.Search.dfs;

public class Sortings {
  public static <V extends Vertex, E extends Edge> List<V> topsort(Graph<V, E> graph, Visitor<V> visitor) {
    List<V> result = new ArrayList<>();
    Pair<Integer, Integer> colors = new Pair<>(1, 2);

    while (result.size() < graph.vertexCount()) {
      V current = graph.getAnyUnusedVertex(result);

      try {
        dfs(current, graph, visitor, colors, null, result::add);
      } catch (CycleDetectedException e) {
        throw new CycleDetectedException("Нельзя топологически отсортировать граф с циклами");
      }
    }

    java.util.Collections.reverse(result);
    return result;
  }
}
