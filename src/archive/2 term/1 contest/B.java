import java.util.*;

public class Main {
  public static void dfs(int current, List<List<Integer>> graph, Set<Integer> colors, List<Integer> result) {
    colors.add(current);
    result.add(current);

    for (var v : graph.get(current)) {
      if (!colors.contains(v)) {
        dfs(v, graph, colors, result);
      }
    }
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    int vertexCount = input.nextInt();
    int edgeCount = input.nextInt();

    List<List<Integer>> graph = new ArrayList<>();
    Set<Integer> colors = new HashSet<>();

    for (int i = 0; i < vertexCount; ++i) {
      graph.add(new ArrayList<>());
    }

    for (int i = 0; i < edgeCount; ++i) {
      int from = input.nextInt() - 1;
      int to = input.nextInt() - 1;

      graph.get(from).add(to);
      graph.get(to).add(from);
    }

    List<List<Integer>> connectivity = new ArrayList<>();

    for (int i = 0; i < vertexCount; ++i) {
      if (!colors.contains(i)) {
        connectivity.add(new ArrayList<>());
        dfs(i, graph, colors, connectivity.get(connectivity.size() - 1));
      }
    }

    System.out.println(connectivity.size());
    for (var basicVertices : connectivity) {
      System.out.println(basicVertices.size());

      for (var v : basicVertices) {
        System.out.print((v + 1) + " ");
      }
      System.out.println();
    }
  }
}
