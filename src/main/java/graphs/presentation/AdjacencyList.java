package graphs.presentation;

import graphs.exceptions.AdjacencyListCreateException;
import graphs.exceptions.EdgeAlreadyExistsException;
import graphs.exceptions.VertexIndexOutOfRangeException;

import java.util.ArrayList;
import java.util.List;

public class AdjacencyList<V extends Vertex, E extends Edge> implements Graph<V, E> {
  private final List<List<V>> list;
  private final List<V> vertexes;

  public AdjacencyList(int vertexCount) throws AdjacencyListCreateException {
    if (vertexCount <= 0) {
      throw new AdjacencyListCreateException("Количество вершин в графе должно быть натуральным числом");
    }

    this.list = new ArrayList<>();
    this.vertexes = new ArrayList<>();

    for (int i = 0; i < vertexCount; ++i) {
      vertexes.add((V) new ColoredVertex(i));
      this.list.add(new ArrayList<>());
    }
  }

  @Override
  public void addEdge(E edge) throws EdgeAlreadyExistsException {
    for (var v : list.get(edge.getFirst().getIndex())) {
      if (v.equals(edge.getSecond())) {
        throw new EdgeAlreadyExistsException("Невозможно добавить: Ребро " + edge + " уже существует");
      }
    }

    list.get(edge.getFirst().getIndex()).add((V) edge.getSecond());
  }

  @Override
  public void removeEdge(E edge) throws EdgeAlreadyExistsException {
    boolean isDeleted = list.get(edge.getFirst().getIndex()).remove((V) edge.getSecond());
    if (!isDeleted) {
      throw new EdgeAlreadyExistsException("Невозможно удалить: Ребро " + edge + " не существует существует");
    }
  }

  @Override
  public boolean isEdge(E edge) {
    return list.get(edge.getFirst().getIndex()).contains((V) edge.getSecond());
  }

  @Override
  public int neighboursCount(V vertex) {
    return list.get(vertex.getIndex()).size();
  }

  @Override
  public List<V> neighbours(V vertex) {
    return List.copyOf(list.get(vertex.getIndex()));
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
  public V getAnyUnusedVertex(Iterable<V> usedVertexes) {
    return null;
  }
}
