package graphs.traversals;

import graphs.exceptions.AdjacencyListCreateException;
import graphs.exceptions.AdjacencyMatrixCreateException;
import graphs.exceptions.EdgeAlreadyExistsException;
import graphs.presentation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SortingsTest {
  private Graph<ColoredVertex, SimpleEdge> dagGraph;

  @BeforeEach
  void setUp() throws EdgeAlreadyExistsException, AdjacencyListCreateException {
    dagGraph = new AdjacencyList<>(5);

    Vertex v1 = new ColoredVertex(0);
    Vertex v2 = new ColoredVertex(1);
    Vertex v3 = new ColoredVertex(2);
    Vertex v4 = new ColoredVertex(3);
    Vertex v5 = new ColoredVertex(4);

    dagGraph.addEdge(new SimpleEdge(v1, v2));
    dagGraph.addEdge(new SimpleEdge(v1, v3));
    dagGraph.addEdge(new SimpleEdge(v3, v4));
    dagGraph.addEdge(new SimpleEdge(v2, v4));
    dagGraph.addEdge(new SimpleEdge(v4, v5));
  }

  @Test
  void testTopSortOnDAG() {
    List<ColoredVertex> sorted = Sortings.topsort(dagGraph);
    System.out.println(sorted);
    assertValidTopologicalSort(dagGraph, sorted);
  }

  @Test
  void testLargeRandomDAG() throws AdjacencyMatrixCreateException, EdgeAlreadyExistsException {
    int size = new Random().nextInt(10_000) + 1;
    Graph<ColoredVertex, SimpleEdge> largeGraph = generateRandomDAG(size);
    List<ColoredVertex> sorted = Sortings.topsort(largeGraph);

    assertEquals(size, sorted.size());
    assertValidTopologicalSort(largeGraph, sorted);
  }

  private Graph<ColoredVertex, SimpleEdge> generateRandomDAG(int size) throws EdgeAlreadyExistsException, AdjacencyMatrixCreateException {
    Graph<ColoredVertex, SimpleEdge> graph = new AdjacencyMatrix<>(size);
    List<ColoredVertex> vertices = new ArrayList<>();

    // Добавляем вершины
    for (int i = 0; i < size; i++) {
      ColoredVertex v = new ColoredVertex(i);
      vertices.add(v);
    }

    Random rand = new Random();
    for (int i = 0; i < size; i++) {
      int edges = rand.nextInt(5);
      for (int j = 0; j < edges; j++) {
        int target = rand.nextInt(size);
        if (i < target) {
          SimpleEdge edge = new SimpleEdge(vertices.get(i), vertices.get(target));
          if (!graph.isEdge(edge)) {
            graph.addEdge(edge);
          }
        }
      }
    }

    return graph;
  }

  private void assertValidTopologicalSort(Graph<ColoredVertex, SimpleEdge> graph, List<ColoredVertex> sorted) {
    Map<ColoredVertex, Integer> position = new HashMap<>();
    for (int i = 0; i < sorted.size(); ++i) {
      position.put(sorted.get(i), i);
    }

    for (int i = 0; i < graph.size(); ++i) {
      ColoredVertex v = new ColoredVertex(i);
      for (ColoredVertex neighbor : graph.neighbours(v)) {
        assertTrue(position.get(v) < position.get(neighbor));
      }
    }
  }
}