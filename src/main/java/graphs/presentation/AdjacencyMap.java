package graphs.presentation;

import graphs.exceptions.AdjacencyListCreateException;
import graphs.exceptions.VertexIndexOutOfRangeException;
import graphs.presentation.factories.GraphComponentsFactory;

import java.util.*;

public class AdjacencyMap<V extends Vertex, E extends Edge> implements Graph<V, E> {
  private final GraphComponentsFactory<V, E> factory;
  private final Map<V, Set<V>> map;

  public AdjacencyMap(Collection<V> vertexes, GraphComponentsFactory<V, E> factory) {
    if (vertexes.isEmpty()) {
      throw new AdjacencyListCreateException("В графе должна быть хотя бы одна вершина");
    }

    this.map = new HashMap<>();
    this.factory = factory;

    for (V vertex : vertexes) {
      this.map.put(vertex, new TreeSet<>());
    }
  }

  @Override
  public void addEdge(E edge) {
    this.map.get(edge.first()).add(factory.createVertex(edge.second().index()));
  }

  @Override
  public void removeEdge(E edge) {
    this.map.get(edge.first()).remove(factory.createVertex(edge.second().index()));
  }

  @Override
  public boolean isEdge(E edge) {
    return this.map.get(edge.first()).contains(factory.createVertex(edge.second().index()));
  }

  @Override
  public int neighboursCount(V vertex) {
    return this.map.get(vertex).size();
  }

  @Override
  public Collection<V> neighbours(V vertex) {
    return this.map.get(vertex);
  }

  @Override
  public V getVertexByIndex(int index) {
    for (V vertex : map.keySet()) {
      if (vertex.index() == index) {
        return vertex;
      }
    }

    throw new VertexIndexOutOfRangeException("Такой вершине нет в графе");
  }

  @Override
  public boolean isVertexIndexExists(int index) {
    for (V vertex : map.keySet()) {
      if (vertex.index() == index) {
        return true;
      }
    }

    return false;
  }

  @Override
  public V getAnyUnusedVertex(Iterable<V> usedVertexes) {
    Map<V, Boolean> used = new HashMap<>();
    for (V v : usedVertexes) {
      used.put(v, true);
    }

    for (V v : map.keySet()) {
      boolean isUsed = used.getOrDefault(v, false);
      if (!isUsed) {
        return v;
      }
    }

    return null;
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public void clear() {
    for (V vertex : map.keySet()) {
      this.map.get(vertex).clear();
    }
  }

  @Override
  public Graph<V, E> transpose() {
    Graph<V, E> result = new AdjacencyMap<>(map.keySet(), factory);

    for (V v : map.keySet()) {
      for (V u : map.get(v)) {
        E edge = factory.createEdge(u, v);
        result.addEdge(edge);
      }
    }

    return result;
  }

  @Override
  public Graph<V, E> subGraph(Collection<V> subVertexes) {
    Graph<V, E> result = new AdjacencyMap<>(subVertexes, factory);

    for (V v : subVertexes) {
      for (V u : map.get(v.index())) {
        result.addEdge(factory.createEdge(v, u));
      }
    }

    return result;
  }
}
