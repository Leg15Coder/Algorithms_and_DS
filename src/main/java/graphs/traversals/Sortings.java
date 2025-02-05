package graphs.traversals;

import graphs.presentation.Edge;
import graphs.presentation.Graph;
import graphs.presentation.Vertex;
import structures.common.Pair;

import java.util.ArrayList;
import java.util.List;

import static graphs.traversals.Search.dfs;

public class Sortings {
  public static <V extends Vertex, E extends Edge> List<V> topsort(Graph<V, E> graph) {
    List<V> result = new ArrayList<>();
    graph.clearColors();
    Pair<Integer, Integer> colors = new Pair<>(1, 2);

    while (result.size() < graph.size()) {
      V current = graph.getAnyUnusedVertex(result);
      dfs(graph, current, colors, null, result::add);
    }

    java.util.Collections.reverse(result);
    return result;
  }
}
