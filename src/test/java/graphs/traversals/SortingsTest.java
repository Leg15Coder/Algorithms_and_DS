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
  private Graph<BasicVertex, BasicEdge> dagGraph;

  @BeforeEach
  void setUp() throws EdgeAlreadyExistsException, AdjacencyListCreateException {
    dagGraph = new AdjacencyList<>(5);

    Vertex v1 = new BasicVertex(0);
    Vertex v2 = new BasicVertex(1);
    Vertex v3 = new BasicVertex(2);
    Vertex v4 = new BasicVertex(3);
    Vertex v5 = new BasicVertex(4);

    dagGraph.addEdge(new BasicEdge(v1, v2));
    dagGraph.addEdge(new BasicEdge(v1, v3));
    dagGraph.addEdge(new BasicEdge(v3, v4));
    dagGraph.addEdge(new BasicEdge(v2, v4));
    dagGraph.addEdge(new BasicEdge(v4, v5));
  }

  @Test
  void testTopSortOnDAG() {
    List<BasicVertex> sorted = Sortings.topsort(dagGraph);
    System.out.println(sorted);
    assertValidTopologicalSort(dagGraph, sorted);
  }

  @Test
  void testLargeRandomDAG() throws AdjacencyMatrixCreateException, EdgeAlreadyExistsException {
    int size = new Random().nextInt(10_000) + 1;
    Graph<BasicVertex, BasicEdge> largeGraph = generateRandomDAG(size);
    List<BasicVertex> sorted = Sortings.topsort(largeGraph);

    assertEquals(size, sorted.size());
    assertValidTopologicalSort(largeGraph, sorted);
  }

  private Graph<BasicVertex, BasicEdge> generateRandomDAG(int size) throws EdgeAlreadyExistsException, AdjacencyMatrixCreateException {
    Graph<BasicVertex, BasicEdge> graph = new AdjacencyMatrix<>(size);
    List<BasicVertex> vertices = new ArrayList<>();

    // Добавляем вершины
    for (int i = 0; i < size; i++) {
      BasicVertex v = new BasicVertex(i);
      vertices.add(v);
    }

    Random rand = new Random();
    for (int i = 0; i < size; i++) {
      int edges = rand.nextInt(5);
      for (int j = 0; j < edges; j++) {
        int target = rand.nextInt(size);
        if (i < target) {
          BasicEdge edge = new BasicEdge(vertices.get(i), vertices.get(target));
          if (!graph.isEdge(edge)) {
            graph.addEdge(edge);
          }
        }
      }
    }

    return graph;
  }

  private void assertValidTopologicalSort(Graph<BasicVertex, BasicEdge> graph, List<BasicVertex> sorted) {
    Map<BasicVertex, Integer> position = new HashMap<>();
    for (int i = 0; i < sorted.size(); ++i) {
      position.put(sorted.get(i), i);
    }

    for (int i = 0; i < graph.size(); ++i) {
      BasicVertex v = new BasicVertex(i);
      for (BasicVertex neighbor : graph.neighbours(v)) {
        assertTrue(position.get(v) < position.get(neighbor));
      }
    }
  }
}