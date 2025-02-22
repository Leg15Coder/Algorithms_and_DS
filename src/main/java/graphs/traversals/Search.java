package graphs.traversals;

import graphs.exceptions.CycleDetectedException;
import graphs.presentation.Edge;
import graphs.presentation.Graph;
import graphs.presentation.Vertex;
import structures.common.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Search {
  public static <V extends Vertex, E extends Edge> void dfs(
      Graph<V, E> graph,
      Map<V, Integer> colorSet,
      V current,
      Pair<Integer, Integer> colors,
      Consumer<V> todoAtEnter,
      Consumer<V> todoAtEnd) {

    if (todoAtEnter != null) {
      todoAtEnter.accept(current);
    }

    int currentColor = colorSet.getOrDefault(current, 0);
    if (currentColor == colors.second()) {
      return;
    }

    if (currentColor == colors.first()) {
      throw new CycleDetectedException("DFS зашёл в цикл");
    }

    colorSet.put(current, colors.first());

    for (var v : graph.neighbours(current)) {
      int vColor = colorSet.getOrDefault(v, 0);
      if (vColor != colors.second()) {
        dfs(graph, colorSet, v, colors, todoAtEnter, todoAtEnd);
      }
    }

    colorSet.put(current, colors.second());
    if (todoAtEnd != null) {
      todoAtEnd.accept(current);
    }
  }
}
