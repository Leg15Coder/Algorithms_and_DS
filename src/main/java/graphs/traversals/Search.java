package graphs.traversals;

import graphs.exceptions.CycleDetectedException;
import graphs.presentation.Edge;
import graphs.presentation.Graph;
import graphs.presentation.Vertex;
import structures.common.Pair;

import java.util.function.Consumer;

public class Search {
  public static <V extends Vertex, E extends Edge> void dfs(
      Graph<V, E> graph,
      V current,
      Pair<Integer, Integer> colors,
      Consumer<V> todoAtEnter,
      Consumer<V> todoAtEnd) throws CycleDetectedException {

    if (todoAtEnter != null) {
      todoAtEnter.accept(current);
    }

    int currentColor = graph.getColor(current);
    if (currentColor == colors.second) {
      // todo exception
    }

    if (currentColor == colors.first()) {
      throw new CycleDetectedException("DFS зашёл в цикл");
    }

    graph.setColor(current, colors.first());

    for (var v : graph.neighbours(current)) {
      int vColor = graph.getColor(v);
      if (vColor != colors.second()) {
        dfs(graph, v, colors, todoAtEnter, todoAtEnd);
      }
    }

    graph.setColor(current, colors.second());
    if (todoAtEnd != null) {
      todoAtEnd.accept(current);
    }
  }
}
