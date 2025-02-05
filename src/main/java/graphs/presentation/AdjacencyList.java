package graphs.presentation;

import graphs.exceptions.AdjacencyListCreateException;
import graphs.exceptions.EdgeAlreadyExistsException;
import graphs.exceptions.VertexIndexOutOfRangeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdjacencyList<V extends Vertex, E extends Edge> implements Graph<V, E> {
  private final List<List<V>> list;
  private final List<V> vertexes;
  private final Map<V, Integer> colors = new HashMap<>();

  public AdjacencyList(int vertexCount) throws AdjacencyListCreateException {
    if (vertexCount <= 0) {
      throw new AdjacencyListCreateException("Количество вершин в графе должно быть натуральным числом");
    }

    this.list = new ArrayList<>();
    this.vertexes = new ArrayList<>();

    for (int i = 0; i < vertexCount; ++i) {
      V vertex = (V) new ColoredVertex(i);
      vertexes.add(vertex);
      this.list.add(new ArrayList<>());
      colors.put(vertex, 0);
    }
  }

  @Override
  public void addEdge(E edge) throws EdgeAlreadyExistsException {
    for (var v : list.get(edge.first().index())) {
      if (v.equals(edge.second())) {
        throw new EdgeAlreadyExistsException("Невозможно добавить: Ребро " + edge + " уже существует");
      }
    }

    list.get(edge.first().index()).add((V) edge.second());
  }

  @Override
  public void removeEdge(E edge) throws EdgeAlreadyExistsException {
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
  public List<V> neighbours(V vertex) {
    return List.copyOf(list.get(vertex.index()));
  }

  @Override
  public V getVertexByIndex(int index) throws VertexIndexOutOfRangeException {
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
  public int getColor(V vertex) {
    return colors.get(vertex);
  }

  @Override
  public void setColor(V vertex, int color) {
    colors.put(vertex, color);
  }

  @Override
  public void clearColors() {
    for (V v : vertexes) {
      colors.put(v, 0);
    }
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
}
