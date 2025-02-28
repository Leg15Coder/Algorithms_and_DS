package graphs.presentation;

import graphs.exceptions.EdgeNotExistsException;
import graphs.exceptions.EdgeOutOfGraphException;
import graphs.exceptions.VertexNotExistsException;
import graphs.presentation.factory.GraphFactory;

import java.util.*;

public class AdjacencyMap<V extends Vertex, E extends Edge> implements Graph<V, E> {
  private final GraphFactory<V, E> factory;
  private final Map<V, List<V>> map;

  public AdjacencyMap(Iterable<V> vertexes, GraphFactory<V, E> graphFactory) {
    this.map = new HashMap<>();
    this.factory = graphFactory;

    for (V vertex : vertexes) {
      this.factory.saveVertex(vertex);
      this.map.put(vertex, new ArrayList<>());
    }
  }

  @Override
  public void addEdge(E edge) {
    V from = (V) edge.from();
    V to = (V) edge.to();

    if (!this.map.containsKey(from) || !this.map.containsKey(to)) {
      throw new EdgeOutOfGraphException("Невозможно добавить данное ребро в граф: один или несколько концов лежат за его пределами");
    }

    this.factory.saveEdge(edge);
    this.map.get(from).add(to);
  }

  @Override
  public void removeEdge(E edge) {
    V from = (V) edge.from();
    V to = (V) edge.to();

    if (!this.map.containsKey(from) || !this.map.containsKey(to)) {
      throw new EdgeOutOfGraphException("В графе нет ребра " + edge);
    }

    boolean result = this.map.get(from).remove(to);
    
    if (!result) {
      throw new EdgeNotExistsException("В графе нет ребра " + edge);
    }
  }

  @Override
  public boolean isEdge(E edge) {
    V from = (V) edge.from();
    V to = (V) edge.to();

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

    return this.map.get(vertex);
  }

  @Override
  public Iterable<E> adjacentEdges(V vertex) {
    List<E> result = new ArrayList<>();
    
    for (V neighbour : neighbours(vertex)) {
      result.add(this.factory.createEdge(vertex, neighbour));
    }
    
    return result;
  }

  @Override
  public V getAnyUnusedVertex(Iterable<V> usedVertexes) {
    Set<V> used = new HashSet<>();
    for (V v : usedVertexes) {
      used.add(v);
    }

    for (V v : map.keySet()) {
      boolean isUsed = used.contains(v);
      if (!isUsed) {
        return v;
      }
    }

    return null;
  }

  @Override
  public E getAnyUnusedEdge(Iterable<E> usedEdges) {
    Set<E> used = new HashSet<>();
    for (E e : usedEdges) {
      used.add(e);
    }

    for (V vertex : getAllVertexes()) {
      for (V neighbour : neighbours(vertex)) {
        E edge = this.factory.createEdge(vertex, neighbour);

        if (!used.contains(edge)) {
          return edge;
        }
      }
    }

    return null;
  }

  @Override
  public Iterable<V> getAllUnusedVertex(Iterable<V> usedVertexes) {
    Map<V, Boolean> used = new HashMap<>();
    for (V v : usedVertexes) {
      used.put(v, true);
    }
    List<V> result = new ArrayList<>();

    for (V v : map.keySet()) {
      boolean isUsed = used.getOrDefault(v, false);
      if (!isUsed) {
        result.add(v);
      }
    }

    return result;
  }

  @Override
  public Iterable<E> getAllUnusedEdge(Iterable<E> usedEdges) {
    Set<E> used = new HashSet<>();
    for (E e : usedEdges) {
      used.add(e);
    }
    List<E> result = new ArrayList<>();

    for (V vertex : getAllVertexes()) {
      for (V neighbour : neighbours(vertex)) {
        E edge = this.factory.createEdge(vertex, neighbour);

        if (!used.contains(edge)) {
          result.add(edge);
        }
      }
    }

    return result;
  }

  @Override
  public Iterable<V> getAllVertexes() {
    return this.map.keySet();
  }

  @Override
  public Iterable<E> getAllEdges() {
    List<E> result = new ArrayList<>();

    for (V vertex : getAllVertexes()) {
      for (V neighbour : neighbours(vertex)) {
        result.add(this.factory.createEdge(vertex, neighbour));
      }
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
  public void clear() {
    this.map.clear();
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
  public Graph<V, E> createSubGraph(Iterable<V> subVertexes) {
    Set<V> sub = new HashSet<>();
    for (V v : subVertexes) {
      sub.add(v);
    }
    
    Graph<V, E> result = new AdjacencyMap<>(subVertexes, factory);

    for (V v : subVertexes) {
      for (V u : map.get(v)) {
        if (sub.contains(u)) {
          result.addEdge(factory.createEdge(v, u));
        }
      }
    }

    return result;
  }
}
