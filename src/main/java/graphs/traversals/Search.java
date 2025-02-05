package graphs.traversals;

import graphs.presentation.Edge;
import graphs.presentation.Graph;
import graphs.presentation.Vertex;
import structures.common.Pair;

public class Search {
  public interface DFSInterface<V extends Vertex> {
     void execute(V current);
  }

  public static <V extends Vertex, E extends Edge> void dfs(
      Graph<V, E> graph,
      V current,
      Pair<Integer, Integer> colors,
      DFSInterface<V> todoAtEnter,
      DFSInterface<V> todoAtEnd) {

    if (graph.getColor(current) == colors.second) {
      // todo exception
    }

    graph.setColor(current, colors.first());
    if (todoAtEnter != null) {
      todoAtEnter.execute(current);
    }

    for (var v : graph.neighbours(current)) {
      int currentColor = graph.getColor(current);
      if (currentColor != colors.first() && currentColor != colors.second()) {
        dfs(graph, v, colors, todoAtEnter, todoAtEnd);
      }
    }

    graph.setColor(current, colors.second());
    if (todoAtEnd != null) {
      todoAtEnd.execute(current);
    }
  }
}
