package graphs.clusterization;

import graphs.presentation.Edge;
import graphs.presentation.Graph;
import graphs.presentation.Vertex;
import graphs.travelling.visitor.NumeratedVisitor;
import graphs.travelling.visitor.RipplesVisitor;
import graphs.travelling.visitor.Visitor;
import structures.common.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static graphs.travelling.Search.dfs;
public class DirectedClusterization {
  public static <V extends Vertex, E extends Edge<V>> List<Set<V>> condensateDirectedGraph(Graph<V, E> graph) {

    if (!graph.isDirected()) {
      throw new IllegalArgumentException(
          "Данный алгоритм конденсирует только ориентированный граф");
    }

    Graph<V, E> transposedGraph = graph.transpose();
    List<Set<V>> result = new ArrayList<>();
    Visitor<V> visitor = new NumeratedVisitor<>();

    int curNumber = 1;
    List<V> travelGuide = new ArrayList<>();

    visitor.update(new Pair<>(curNumber, curNumber));

    // Процесс обхода графа для нахождения вершин в компонентах сильной связности
    for (V vertex : graph.getAllVertexes()) {
      if ((int) visitor.getState(vertex) != 1) {
        dfs(vertex, graph, visitor, null, travelGuide::add);
      }
    }

    Collections.reverse(travelGuide);
    Set<V> used = new HashSet<>();
    visitor = new RipplesVisitor<>(used);
    Visitor<V> transposedVisitor = new RipplesVisitor<>(used);

    for (V vertex : travelGuide) {
      if (!used.contains(vertex)) {
        Set<V> achievable = new HashSet<>();
        result.add(new HashSet<>());

        Pair<Integer, Integer> curNumbersState = new Pair<>(curNumber, curNumber);
        visitor.update(curNumbersState);
        transposedVisitor.update(curNumbersState);

        dfs(vertex, graph, visitor, achievable::add, null);
        dfs(
            vertex,
            transposedGraph,
            transposedVisitor,
            (v) -> {
              if (achievable.contains(v)) {
                used.add(v);
                result.get(curNumbersState.second() - 1).add(v);
              }
            },
            null);

        ++curNumber;
      }
    }

    return result;
  }
}
