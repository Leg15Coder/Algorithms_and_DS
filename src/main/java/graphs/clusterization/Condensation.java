package graphs.clusterization;

import graphs.presentation.*;
import structures.common.Pair;

import java.util.*;

import static graphs.traversals.Search.dfs;

public class Condensation {
  public static <V extends Vertex, E extends Edge> List<Set<V>> identifyConnectivityComponents(Graph<V, E> graph) {
    List<Set<V>> result = new ArrayList<>();
    int color = 1;

    for (int i = 0; i < graph.size(); ++i) {
      V vertex = graph.getVertexByIndex(i);
      if (graph.getColor(vertex) == 0) {
        result.add(new HashSet<>());
        Pair<Integer, Integer> curColor = new Pair<>(color, color);

        dfs(graph, vertex, curColor, result.get(curColor.first() - 1)::add, null);
        ++color;
      }
    }

    return result;
  }

  public static <V extends Vertex, E extends Edge> List<Set<V>> condensateDirectedGraph(Graph<V, E> graph) {
    graph.clearColors();
    Graph<V, E> transposedGraph;
    try {
      transposedGraph = graph.transpose();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    int color = 1;
    List<Set<V>> result = new ArrayList<>();

    for (int i = 0; i < graph.size(); ++i) {
      V vertex = graph.getVertexByIndex(i);
      if (graph.getColor(vertex) == 0) {
        Set<V> achievable = new HashSet<>();
        result.add(new HashSet<>());
        Pair<Integer, Integer> curColor = new Pair<>(color, color);

        dfs(graph, vertex, curColor, achievable::add, null);
        dfs(transposedGraph, vertex, curColor, (v) -> {
          if (achievable.contains(v)) {
            result.get(curColor.first() - 1).add(v);
          }
        }, null);
        ++color;
      }
    }

    return result;
  }

  private static <V extends Vertex> int getComponentByVertex(V vertex, List<Set<V>> condensation) {
    int index = 0;
    for (var s : condensation) {
      if (s.contains(vertex)) {
        return index;
      }

      ++index;
    }

    throw new NoSuchElementException("В конденсации нет данной вершины");
  }

  public static <V extends Vertex, E extends Edge> Graph<V, E> buildCondensedGraph(Graph<V, E> graph) {
    List<Set<V>> condensation = condensateDirectedGraph(graph);
    Graph<V, E> result = new AdjacencyList<>(condensation.size());
    Map<V, Integer> lru = new HashMap<>();

    for (int i = 0; i < result.size(); ++i) {
      V vertex = graph.getVertexByIndex(i);
      List<V> neighbours = result.neighbours(vertex);

      for (V neighbour : neighbours) {
        int vertexComponent = lru.getOrDefault(vertex, getComponentByVertex(vertex, condensation));
        lru.put(vertex, vertexComponent);
        int neighbourComponent = lru.getOrDefault(neighbour, getComponentByVertex(neighbour, condensation));
        lru.put(neighbour, neighbourComponent);

        if (vertexComponent != neighbourComponent) {
          V from = result.getVertexByIndex(vertexComponent);
          V to = result.getVertexByIndex(neighbourComponent);

          E edge = (E) new BasicEdge(from, to);
          if (!result.isEdge(edge)) {
            result.addEdge(edge);
          }
        }
      }
    }

    return result;
  }
}
