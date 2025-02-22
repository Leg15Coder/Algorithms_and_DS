package graphs.presentation;

import graphs.exceptions.AdjacencyMatrixCreateException;
import graphs.exceptions.EdgeAlreadyExistsException;
import graphs.exceptions.VertexIndexOutOfRangeException;
import graphs.presentation.factories.GraphComponentsFactory;

import java.util.*;

public class AdjacencyMatrix<V extends Vertex, E extends Edge> implements Graph<V, E> {
  private final GraphComponentsFactory<V, E> factory;
  private boolean[][] matrix;
  private final List<V> vertexes;

  public AdjacencyMatrix(int vertexCount, GraphComponentsFactory<V, E> factory) {
    if (vertexCount <= 0) {
      throw new AdjacencyMatrixCreateException("Количество вершин в графе должно быть натуральным числом");
    }

    this.vertexes = new ArrayList<>();
    this.factory = factory;

    for (int i = 0; i < vertexCount; ++i) {
      V vertex = factory.createVertex(i);
      vertexes.add(vertex);
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
  public Collection<V> neighbours(V vertex) {
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
    Graph<V, E> result = new AdjacencyMatrix<>(size(), factory);

    for (int v = 0; v < size(); ++v) {
      for (int u = 0; u < size(); ++u) {
        if (v != u && !matrix[v][u]) {
          V first = getVertexByIndex(v);
          V second = getVertexByIndex(u);
          result.addEdge(factory.createEdge(first, second));
        }
      }
    }

    return result;
  }

  @Override
  public Graph<V, E> subGraph(Collection<V> subVertexes) {
    Graph<V, E> result = new AdjacencyMap<>(subVertexes, factory);

    for (V v : subVertexes) {
      int vIndex = v.index();
      for (int i = 0; i < size(); ++i) {
        if (matrix[vIndex][i]) {
          result.addEdge(factory.createEdge(v, factory.createVertex(i)));
        }
      }
    }

    return result;
  }
}
