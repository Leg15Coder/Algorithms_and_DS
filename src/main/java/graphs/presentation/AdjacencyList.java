package graphs.presentation;

import graphs.exceptions.AdjacencyListCreateException;
import graphs.exceptions.EdgeAlreadyExistsException;
import graphs.exceptions.VertexIndexOutOfRangeException;
import graphs.presentation.factories.GraphComponentsFactory;

import java.util.*;

public class AdjacencyList<V extends Vertex, E extends Edge> implements Graph<V, E> {
  private final GraphComponentsFactory<V, E> factory;
  private final List<List<V>> list;
  private final List<V> vertexes;

  public AdjacencyList(int vertexCount, GraphComponentsFactory<V, E> factory) {
    if (vertexCount <= 0) {
      throw new AdjacencyListCreateException("Количество вершин в графе должно быть натуральным числом");
    }

    this.list = new ArrayList<>();
    this.vertexes = new ArrayList<>();
    this.factory = factory;

    for (int i = 0; i < vertexCount; ++i) {
      V vertex = factory.createVertex(i);
      vertexes.add(vertex);
      this.list.add(new ArrayList<>());
    }
  }

  @Override
  public void addEdge(E edge) {
    list.get(edge.first().index()).add((V) edge.second());
  }

  @Override
  public void removeEdge(E edge) {
    boolean isDeleted = list.get(edge.first().index()).remove((V) edge.second());
    if (!isDeleted) {
      throw new EdgeAlreadyExistsException("Невозможно удалить: Ребро " + edge + " не существует существует");
    }
  }

  @Override
  public boolean isEdge(E edge) {
    return list.get(edge.first().index()).contains((V) edge.second());
  }

  @Override
  public int neighboursCount(V vertex) {
    return list.get(vertex.index()).size();
  }

  @Override
  public Collection<V> neighbours(V vertex) {
    return list.get(vertex.index());
  }

  @Override
  public V getVertexByIndex(int index) {
    if (!isVertexIndexExists(index)){
      throw new VertexIndexOutOfRangeException("Такой вершине нет в графе");
    }

    return vertexes.get(index);
  }

  @Override
  public boolean isVertexIndexExists(int index) {
    return index >= 0 && index < vertexes.size();
  }

  @Override
  public V getAnyUnusedVertex(Iterable<V> usedVertexes) {
    Map<V, Boolean> used = new HashMap<>();
    for (V v : usedVertexes) {
      used.put(v, true);
    }

    for (V v : vertexes) {
      boolean isUsed = used.getOrDefault(v, false);
      if (!isUsed) {
        return v;
      }
    }

    return null;
  }

  @Override
  public int size() {
    return vertexes.size();
  }

  @Override
  public void clear() {
    for (var l : list) {
      l.clear();
    }
  }

  @Override
  public Graph<V, E> transpose() {
    Graph<V, E> result = new AdjacencyList<>(size(), factory);

    for (int v = 0; v < size(); ++v) {
      for (V second : list.get(v)) {
        V first = getVertexByIndex(v);

        result.addEdge(factory.createEdge(second, first));
      }
    }

    return result;
  }

  @Override
  public Graph<V, E> subGraph(Collection<V> subVertexes) {
    Graph<V, E> result = new AdjacencyMap<>(subVertexes, factory);

    for (V v : subVertexes) {
      for (V u : list.get(v.index())) {
        result.addEdge(factory.createEdge(v, u));
      }
    }

    return result;
  }
}
