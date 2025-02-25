import java.util.*;
import java.util.function.Consumer;

public class Main {
  public static class GraphException extends Exception {
    public GraphException(String message) {
      super(message);
    }
  }

  public static class CycleDetectedException extends GraphException {
    public CycleDetectedException(String message) {
      super(message);
    }
  }

  public static class EdgeAlreadyExistsException extends GraphException {
    public EdgeAlreadyExistsException(String message) {
      super(message);
    }
  }

  public static class EdgeNotExistsException extends GraphException {
    public EdgeNotExistsException(String message) {
      super(message);
    }
  }

  public static class VertexIndexOutOfRangeException extends GraphException {
    public VertexIndexOutOfRangeException(String message) {
      super(message);
    }
  }

  public interface PairInterface<T, E> {
    T first();

    E second();
  }

  public static class Pair<T, E> implements PairInterface<T, E> {
    public final T first;
    public final E second;

    public Pair(T first, E second) {
      this.first = first;
      this.second = second;
    }

    @Override
    public T first() {
      return first;
    }

    @Override
    public E second() {
      return second;
    }
  }

  public interface Graph<V extends Vertex, E extends Edge> {
    void addEdge(E edge) throws EdgeAlreadyExistsException;

    void removeEdge(E edge) throws EdgeAlreadyExistsException;

    boolean isEdge(E edge);

    int neighboursCount(V vertex);

    List<V> neighbours(V vertex);

    V getVertexByIndex(int index) throws VertexIndexOutOfRangeException;

    boolean isVertexIndexExists(int index);

    int getColor(V vertex);

    void setColor(V vertex, int color);

    void clearColors();

    V getAnyUnusedVertex(Iterable<V> usedVertexes);

    int size();

    void clear();
  }

  public interface Vertex {
    int index();
  }

  public interface Edge extends PairInterface<Vertex, Vertex> { }

  public static class GraphCreateException extends GraphException {
    public GraphCreateException(String message) {
      super(message);
    }
  }

  public static class AdjacencyListCreateException extends GraphCreateException {
    public AdjacencyListCreateException(String message) {
      super(message);
    }
  }

  public static class AdjacencyList<V extends Vertex, E extends Edge> implements Graph<V, E> {
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
      return list.get(vertex.index());
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

  public record ColoredVertex(int index) implements Vertex {
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof ColoredVertex that)) return false;
      return index == that.index;
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(index);
    }
  }

  public record SimpleEdge(Vertex first, Vertex second) implements Edge {
    @Override
    public Vertex first() {
      return first;
    }

    @Override
    public Vertex second() {
      return second;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof SimpleEdge that)) return false;
      return Objects.equals(first, that.first) && Objects.equals(second, that.second);
    }

    @Override
    public int hashCode() {
      return Objects.hash(first, second);
    }
  }

  public static <V extends Vertex, E extends Edge> void dfs(
      Graph<V, E> graph,
      V current,
      Pair<Integer, Integer> colors,
      Consumer<V> todoAtEnter,
      Consumer<V> todoAtEnd) throws CycleDetectedException {

    if (todoAtEnter != null) {
      todoAtEnter.accept(current);
    }

    int currentColor = graph.getColor(current);

    if (currentColor == colors.first()) {
      throw new CycleDetectedException("DFS зашёл в цикл");
    }

    graph.setColor(current, colors.first());

    for (var v : graph.neighbours(current)) {
      int vColor = graph.getColor(v);
      if (vColor != colors.second()) {
        dfs(graph, v, colors, todoAtEnter, todoAtEnd);
      }
    }

    graph.setColor(current, colors.second());
    if (todoAtEnd != null) {
      todoAtEnd.accept(current);
    }
  }

  public static <V extends Vertex, E extends Edge> List<V> getAnyCycle(Graph<V, E> graph) throws VertexIndexOutOfRangeException {
    graph.clearColors();
    Pair<Integer, Integer> colors = new Pair<>(1, 2);
    List<V> visited = new ArrayList<>();
    Set<V> toDelete = new HashSet<>();

    for (int i = 0; i < graph.size(); ++i) {
      V vertex = graph.getVertexByIndex(i);
      if (graph.getColor(vertex) != 2) {
        try {
          dfs(graph, vertex, colors, visited::add, toDelete::add);
        } catch (CycleDetectedException e) {
          V last = visited.get(visited.size() - 1);
          List<V> result = new ArrayList<>();
          boolean startCycle = false;

          for (V v : visited) {
            if (v.equals(last)) {
              if (startCycle) {
                return result;
              } else {
                result.add(v);
                startCycle = true;
              }
            } else if (startCycle && !toDelete.contains(v)) {
              result.add(v);
            }
          }
        }
      }
    }

    return null;
  }

  public static void main(String[] args) throws AdjacencyListCreateException, VertexIndexOutOfRangeException {
    Scanner input = new Scanner(System.in);

    int vertexCount = input.nextInt();
    int edgeCount = input.nextInt();

    Graph<ColoredVertex, SimpleEdge> graph = new AdjacencyList<>(vertexCount);

    for (int i = 0; i < edgeCount; ++i) {
      ColoredVertex from = new ColoredVertex(input.nextInt() - 1);
      ColoredVertex to = new ColoredVertex(input.nextInt() - 1);

      try {
        graph.addEdge(new SimpleEdge(from, to));
      } catch (EdgeAlreadyExistsException e) {
        continue;
      }
    }

    List<ColoredVertex> cycle = getAnyCycle(graph);

    if (cycle != null) {
      System.out.println("YES");

      for (var v : cycle) {
        System.out.print((v.index() + 1) + " ");
      }
    } else {
      System.out.println("NO");
    }
  }
}
