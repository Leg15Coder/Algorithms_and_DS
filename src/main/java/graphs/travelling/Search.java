package graphs.travelling;

import graphs.exceptions.BFSVisitException;
import graphs.exceptions.DFSVisitException;
import graphs.presentation.Edge;
import graphs.presentation.Graph;
import graphs.presentation.Vertex;
import graphs.travelling.visitor.VisitState;
import graphs.travelling.visitor.Visitor;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

public class Search {
  public static <V extends Vertex, E extends Edge<V>> void dfs(
      V current,
      Graph<V, E> graph,
      Visitor visitor,
      Consumer<V> todoAtEnter,
      Consumer<V> todoAtEnd) {

    VisitState visitState = visitor.visit(current);
    if (visitState == VisitState.INNOCENT) {
      return;
    } else if (visitState == VisitState.INCORRECT) {
      throw new DFSVisitException("Ошибка при обработке вершины графа");
    }

    if (todoAtEnter != null) {
      todoAtEnter.accept(current);
    }

    for (var v : graph.neighbours(current)) {
      boolean isUnvisited = visitor.goToNext(current, v);
      if (isUnvisited) {
        dfs(v, graph, visitor, todoAtEnter, todoAtEnd);
      }
    }

    if (todoAtEnd != null) {
      todoAtEnd.accept(current);
    }
    visitor.leave(current);
  }

  public static <V extends Vertex, E extends Edge<V>> void bfs(V start, Graph<V, E> graph, Visitor visitor) {
    Queue<V> wave = new LinkedList<>();
    wave.add(start);

    while (!wave.isEmpty()) {
      V current = wave.remove();
      VisitState visitState = visitor.visit(current);

      if (visitState == VisitState.INNOCENT) {
        continue;
      } else if (visitState == VisitState.INCORRECT) {
        throw new BFSVisitException("Ошибка при обработке вершины графа");
      }

      for (var v : graph.neighbours(current)) {
        boolean isUnvisited = visitor.goToNext(current, v);
        if (isUnvisited) {
          wave.add(v);
        }
      }

      visitor.leave(current);
    }
  }
}
