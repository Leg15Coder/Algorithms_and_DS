package graphs.presentation;

import graphs.exceptions.AdjacencyMatrixCreateException;
import graphs.exceptions.EdgeAlreadyExistsException;
import graphs.exceptions.VertexIndexOutOfRangeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdjacencyMatrix<V extends Vertex, E extends Edge> implements Graph<V, E> {
  private boolean[][] matrix;
  private final List<V> vertexes;
  private final Map<V, Integer> colors = new HashMap<>();

  public AdjacencyMatrix(int vertexCount) {
    if (vertexCount <= 0) {
      throw new AdjacencyMatrixCreateException("Количество вершин в графе должно быть натуральным числом");
    }

    this.vertexes = new ArrayList<>();
    for (int i = 0; i < vertexCount; ++i) {
      V vertex = (V) new BasicVertex(i);
      vertexes.add(vertex);
      colors.put(vertex, 0);
    }

    this.matrix = new boolean[vertexCount][vertexCount];
  }

  @Override
  public void addEdge(E edge) {
    if (matrix[edge.first().index()][edge.second().index()]) {
      throw new EdgeAlreadyExistsException("Невозможно добавить: Ребро " + edge + " уже существует");
    }

    matrix[edge.first().index()][edge.second().index()] = true;
  }

  @Override
  public void removeEdge(E edge) {
    if (matrix[edge.first().index()][edge.second().index()]) {
      throw new EdgeAlreadyExistsException("Невозможно удалить: Ребро " + edge + " не существует существует");
    }

    matrix[edge.first().index()][edge.second().index()] = true;
  }

  @Override
  public boolean isEdge(E edge) {
    return matrix[edge.first().index()][edge.second().index()];
  }

  @Override
  public int neighboursCount(V vertex) {
    int result = 0;

    for (int i = 0; i < matrix.length; ++i) {
      if (i != vertex.index() && matrix[vertex.index()][i]) {
        ++result;
      }
    }

    return result;
  }

  @Override
  public List<V> neighbours(V vertex) {
    List<V> result = new ArrayList<>();

    for (int i = 0; i < matrix.length; ++i) {
      if (i != vertex.index() && matrix[vertex.index()][i]) {
        result.add(vertexes.get(i));
      }
    }

    return result;
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
  public int getColor(V vertex) {
    return colors.get(vertex);
  }

  @Override
  public void setColor(V vertex, int color) {
    colors.put(vertex, color);
  }

  @Override
  public void clearColors() {
    for (int i = 0; i < size(); ++i) {
      V vertex = (V) new BasicVertex(i);
      colors.put(vertex, 0);
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
    this.matrix = new boolean[size()][size()];
  }

  @Override
  public Graph<V, E> transpose() {
    Graph<V, E> result = new AdjacencyMatrix<>(size());

    for (int v = 0; v < size(); ++v) {
      for (int u = 0; u < size(); ++u) {
        if (v != u && !matrix[v][u]) {
          V first = getVertexByIndex(v);
          V second = getVertexByIndex(u);
          result.addEdge((E) new BasicEdge(first, second));
        }
      }
    }

    return result;
  }
}
