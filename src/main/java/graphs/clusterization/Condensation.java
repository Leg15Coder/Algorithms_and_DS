package graphs.clusterization;

import graphs.presentation.*;
import graphs.presentation.factories.GraphComponentsFactory;
import graphs.presentation.factories.SubgraphFactory;
import structures.common.Pair;

import java.util.*;

import static graphs.traversals.Search.dfs;

public class Condensation {
  public static <V extends Vertex, E extends Edge> List<Set<V>> identifyConnectivityComponents(Graph<V, E> graph) {
    List<Set<V>> result = new ArrayList<>();
    Map<V, Integer> colorSet = new HashMap<>();

    int color = 1;

    for (int i = 0; i < graph.size(); ++i) {
      V vertex = graph.getVertexByIndex(i);
      if (colorSet.getOrDefault(vertex, 0) == 0) {
        result.add(new HashSet<>());
        Pair<Integer, Integer> curColor = new Pair<>(color, color);

        dfs(graph, colorSet, vertex, curColor, result.get(color - 1)::add, null);
        ++color;
      }
    }

    return result;
  }

  public static <V extends Vertex, E extends Edge> List<Set<V>> condensateDirectedGraph(Graph<V, E> graph) {
    Map<V, Integer> colorSet = new HashMap<>();
    Map<V, Integer> transposedColorSet = new HashMap<>();
    Graph<V, E> transposedGraph = graph.transpose();

    int color = 1;
    List<Set<V>> result = new ArrayList<>();
    Set<V> used = new HashSet<>();

    for (int i = 0; i < graph.size(); ++i) {
      V vertex = graph.getVertexByIndex(i);
      if (!used.contains(vertex)) {
        Set<V> achievable = new HashSet<>();
        result.add(new HashSet<>());
        Pair<Integer, Integer> curColor = new Pair<>(color, color);

        dfs(graph, colorSet, vertex, curColor, (v) -> {
          if (used.contains(v)) {
            colorSet.put(v, curColor.second());
          } else {
            achievable.add(v);
          }
        }, null);

        dfs(transposedGraph, transposedColorSet, vertex, curColor, (v) -> {
          if (used.contains(v)) {
            colorSet.put(v, curColor.second());
          } else if (achievable.contains(v)) {
            used.add(v);
            result.get(curColor.second() - 1).add(v);
          }
        }, null);

        ++color;
      }
    }

    return result;
  }

  public static <V extends Vertex, E extends Edge> Graph<GraphVertex<V, E>, GraphEdge> buildCondensedGraph(Graph<V, E> graph) {
    List<Set<V>> condensation = condensateDirectedGraph(graph);
    SubgraphFactory<V, E> factory = new SubgraphFactory<>();
    Map<V, Integer> colorSet = new HashMap<>();

    var ref = new Object() {
      int countComponents = 0;
    };

    Graph<GraphVertex<V, E>, GraphEdge> result = new AdjacencyMap<>(
        condensation.stream()
            .map(graph::subGraph)
            .map((tmpGraph) -> factory.save(ref.countComponents++, tmpGraph))
            .toList(),
        factory);


    for (int i = 0; i < result.size(); ++i) {
      ref.countComponents++;
      V vertex = condensation.get(i).iterator().next();
      var colors = new Pair<>(ref.countComponents, ref.countComponents);

      int finalI = i;
      dfs(graph, colorSet, vertex, colors, (v) -> {
        if (!condensation.get(finalI).contains(v)) {
          for (int j = 0; j < result.size(); ++j) {
            if (finalI != j && condensation.get(j).contains(v)) {
              result.addEdge(new GraphEdge(
                  factory.createVertex(finalI),
                  factory.createVertex(j)
              ));

              colorSet.put(v, ref.countComponents);
              return;
            }
          }
        }
      }, null);
    }

    return result;
  }
}
