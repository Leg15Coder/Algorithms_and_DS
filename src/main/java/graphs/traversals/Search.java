package graphs.traversals;

import graphs.exceptions.CycleDetectedException;
import graphs.presentation.Edge;
import graphs.presentation.Graph;
import graphs.presentation.Vertex;
import graphs.presentation.factory.GraphFactory;
import graphs.presentation.visitors.Visitor;
import structures.common.Pair;

import java.util.function.Consumer;

public class Search {
  public static <V extends Vertex, E extends Edge> void dfs(
      V current,
      Graph<V, E> graph,
      Visitor visitor,
      Pair<Integer, Integer> colors,
      Consumer<V> todoAtEnter,
      Consumer<V> todoAtEnd) {

    if (todoAtEnter != null) {
      todoAtEnter.accept(current);
    }

    int currentColor = visitor.visit(current);
    if (currentColor == colors.second()) {
      return;
    }

    if (currentColor == colors.first()) {
      throw new CycleDetectedException("DFS зашёл в цикл");
    }

    visitor.update(current, colors.first());

    for (var v : graph.neighbours(current)) {
      int vColor = visitor.getState(v);
      if (vColor != colors.second()) {
        dfs(v, graph, visitor, colors, todoAtEnter, todoAtEnd);
      }
    }

    visitor.leave(current, colors.second());
    if (todoAtEnd != null) {
      todoAtEnd.accept(current);
    }
  }
}
