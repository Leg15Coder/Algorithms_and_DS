package graphs.presentation;

import graphs.exceptions.EdgeNotExistsException;
import graphs.exceptions.EdgeOutOfGraphException;
import graphs.exceptions.VertexNotExistsException;
import graphs.presentation.factory.BasicEdge;
import graphs.presentation.factory.GraphFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphOnAdjacencyMap<V extends Vertex, E extends Edge<V>> implements Graph<V, E> {
  private final GraphFactory<V, E> factory;
  private final Map<V, List<E>> map;
  private final boolean isDirected;

  public GraphOnAdjacencyMap(
      Iterable<V> vertexes, GraphFactory<V, E> graphFactory, boolean isDirected) {
    this.map = new HashMap<>();
    this.factory = graphFactory;
    this.isDirected = isDirected;

    for (V vertex : vertexes) {
      this.factory.saveVertex(vertex);
      this.map.put(vertex, new ArrayList<>());
    }
  }

  public GraphOnAdjacencyMap(GraphFactory<V, E> graphFactory, boolean isDirected) {
    this.map = new HashMap<>();
    this.factory = graphFactory;
    this.isDirected = isDirected;
  }

  @Override
  public void addEdge(E edge) {
    V from = edge.from();
    V to = edge.to();

    if (!this.map.containsKey(from) || !this.map.containsKey(to)) {
      throw new EdgeOutOfGraphException(
          "Невозможно добавить данное ребро в граф: один или несколько концов лежат за его"
              + " пределами");
    }

    this.factory.saveEdge(edge);
    this.map.get(from).add(edge);

    // Если граф неориентированный, добавляем обратную версию ребра
    if (!this.isDirected) {
      E reversedEdge = (E) ((BasicEdge<?>) edge).reverseArguments();
      this.factory.saveEdge(reversedEdge);
      this.map.get(to).add((E) edge.reverseArguments());
    }
  }

  @Override
  public void removeEdge(E edge) {
    V from = edge.from();
    V to = edge.to();

    // Проверка существования ребра
    if (!this.map.containsKey(from) || !this.map.containsKey(to)) {
      throw new EdgeOutOfGraphException("В графе нет ребра " + edge);
    }

    boolean result = this.map.get(from).remove(to);

    // Если граф неориентированный, удаляем обратную версию ребра
    if (result && !this.isDirected) {
      this.map.get(to).remove(from);
    }

    if (!result) {
      throw new EdgeNotExistsException("В графе нет ребра " + edge);
    }
  }

  @Override
  public boolean isEdge(E edge) {
    V from = edge.from();
    V to = edge.to();

    if (!this.map.containsKey(from) || !this.map.containsKey(to)) {
      return false;
    }

    return this.map.get(from).contains(to);
  }

  @Override
  public void addVertex(V vertex) {
    this.factory.saveVertex(vertex);
    this.map.put(vertex, this.map.getOrDefault(vertex, new ArrayList<>()));
  }

  @Override
  public void removeVertex(V vertex) {
    this.map.remove(vertex);
  }

  @Override
  public boolean isVertexExists(V vertex) {
    return this.map.containsKey(vertex);
  }

  @Override
  public int neighboursCount(V vertex) {
    if (!isVertexExists(vertex)) {
      throw new VertexNotExistsException("В графе нет вершины " + vertex);
    }

    return this.map.get(vertex).size();
  }

  @Override
  public Iterable<V> neighbours(V vertex) {
    if (!isVertexExists(vertex)) {
      throw new VertexNotExistsException("В графе нет вершины " + vertex);
    }

    return this.map.get(vertex).stream().map(E::to).toList();
  }

  @Override
  public Iterable<E> adjacentEdges(V vertex) {
    return this.map.get(vertex);
  }

  @Override
  public Iterable<V> getAllVertexes() {
    return this.map.keySet();
  }

  @Override
  public Iterable<E> getAllEdges() {
    List<E> result = new ArrayList<>();

    // Для каждой вершины добавляем её рёбра
    for (V vertex : getAllVertexes()) {
      result.add((E) this.map.get(vertex));
    }

    return result;
  }

  @Override
  public int vertexCount() {
    return map.size();
  }

  @Override
  public int edgesCount() {
    int result = 0;

    for (V vertex : getAllVertexes()) {
      result += neighboursCount(vertex);
    }

    return result;
  }

  @Override
  public boolean isDirected() {
    return isDirected;
  }

  @Override
  public void clear() {
    this.map.clear();
  }

  @Override
  public Graph<V, E> transpose() {
    GraphOnAdjacencyMap<V, E> result =
        new GraphOnAdjacencyMap<>(map.keySet(), factory, isDirected);

    // Транспонируем граф (меняем направления рёбер)
    for (V v : map.keySet()) {
      for (E u : map.get(v)) {
        if (!result.map.containsKey(u)) {
          result.map.put(u.to(), new ArrayList<>());
        }

        result.map.get(u).add((E) u.reverseArguments().from());
      }
    }

    return result;
  }

  @Override
  public Graph<V, E> createSubGraph(Iterable<V> subVertexes) {
    Set<V> sub = new HashSet<>();
    for (V v : subVertexes) {
      sub.add(v);
    }

    Graph<V, E> result = new GraphOnAdjacencyMap<>(subVertexes, factory, isDirected);

    // Создаём подграф только с выбранными вершинами и рёбрами
    for (V v : subVertexes) {
      for (E u : map.get(v)) {
        if (sub.contains(u.to())) {
          result.addEdge(u);
        }
      }
    }

    return result;
  }
}
