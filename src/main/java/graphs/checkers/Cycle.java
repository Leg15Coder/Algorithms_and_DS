package graphs.checkers;

import graphs.exceptions.CycleDetectedException;
import graphs.exceptions.VertexIndexOutOfRangeException;
import graphs.presentation.Edge;
import graphs.presentation.Graph;
import graphs.presentation.Vertex;
import structures.common.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static graphs.traversals.Search.dfs;

public class Cycle {
  public static <V extends Vertex, E extends Edge> boolean hasCycle(Graph<V, E> graph) {
    graph.clearColors();
    Pair<Integer, Integer> colors = new Pair<>(1, 2);

    for (int i = 0; i < graph.size(); ++i) {
      V vertex = graph.getVertexByIndex(i);
      if (graph.getColor(vertex) != 2) {
        try {
          dfs(graph, vertex, colors, null, null);
        } catch (CycleDetectedException e) {
          return true;
        }
      }
    }

    return false;
  }

  public static <V extends Vertex, E extends Edge> List<V> getAnyCycle(Graph<V, E> graph) {
    graph.clearColors();
    Pair<Integer, Integer> colors = new Pair<>(1, 2);
    List<V> visited = new ArrayList<>();
    Set<V> toDelete = new HashSet<>();

    for (int i = 0; i < graph.size(); ++i) {
      V vertex = graph.getVertexByIndex(i);
      if (graph.getColor(vertex) != 2) {
        try {
          dfs(graph, vertex, colors, visited::add, toDelete::add);
        } catch (CycleDetectedException e) {
          V last = visited.get(visited.size() - 1);
          List<V> result = new ArrayList<>();
          boolean startCycle = false;

          for (V v : visited) {
            if (v.equals(last)) {
              if (startCycle) {
                return result;
              } else {
                result.add(v);
                startCycle = true;
              }
            } else if (startCycle && !toDelete.contains(v)) {
              result.add(v);
            }
          }
        }
      }
    }

    return null;
  }
}
