package graphs.checkers;

import graphs.exceptions.CycleDetectedException;
import graphs.presentation.Edge;
import graphs.presentation.Graph;
import graphs.presentation.Vertex;
import graphs.presentation.visitors.Visitor;
import structures.common.Pair;

import java.util.ArrayList;
import java.util.List;

import static graphs.traversals.Search.dfs;

public class Trees {
  public static <V extends Vertex, E extends Edge> boolean isTree(Graph<V, E> graph, Visitor<V> visitor) {
    Pair<Integer, Integer> colors = new Pair<>(1, 1);
    List<V> visited = new ArrayList<>();

    try {
      dfs(graph.getAnyUnusedVertex(new ArrayList<>()), graph, visitor, colors, visited::add, null);
    } catch (CycleDetectedException e) {
      return false;
    }

    return visited.size() == graph.vertexCount();
  }
}
