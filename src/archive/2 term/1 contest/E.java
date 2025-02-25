import java.util.*;

public class Main {
  public static int globalTime = 0;

  public static <T extends Comparable<T>> T min(T l, T r) {
    if (l == null) {
      return r;
    }
    if (r == null) {
      return l;
    }
    return (l.compareTo(r) > 0) ? r : l;
  }

  public static void dfs(
      int current,
      int parent,
      List<List<Integer>> graph,
      Set<Integer> colors,
      List<Integer> enterTime,
      List<Integer> minDescTime,
      Set<Integer> result) {

    colors.add(current);
    enterTime.set(current, ++globalTime);
    minDescTime.set(current, min(minDescTime.get(current), globalTime));
    int unUniteNeighbours = 0;

    for (var v : graph.get(current)) {
      if (v != parent) {
        minDescTime.set(current, min(minDescTime.get(current), enterTime.get(v)));

        if (!colors.contains(v)) {
          ++unUniteNeighbours;
          dfs(v, current, graph, colors, enterTime, minDescTime, result);
          minDescTime.set(current, min(minDescTime.get(current), minDescTime.get(v)));

          if (parent >= 0 && minDescTime.get(v) >= enterTime.get(current)) {
            result.add(current);
          }
        }
      }
    }

    if (parent < 0 && unUniteNeighbours > 1) {
      result.add(current);
    }
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    int vertexCount = input.nextInt();
    int edgeCount = input.nextInt();

    List<List<Integer>> graph = new ArrayList<>();
    Set<Integer> colors = new HashSet<>();
    List<Integer> enterTime = new ArrayList<>();
    List<Integer> minDescTime = new ArrayList<>();
    Set<Integer> result = new HashSet<>();

    for (int i = 0; i < vertexCount; ++i) {
      graph.add(new ArrayList<>());
      enterTime.add(null);
      minDescTime.add(null);
    }

    for (int i = 0; i < edgeCount; ++i) {
      int from = input.nextInt() - 1;
      int to = input.nextInt() - 1;

      graph.get(from).add(to);
      graph.get(to).add(from);
    }

    for (int i = 0; i < vertexCount; ++i) {
      if (!colors.contains(i)) {
        dfs(i, -1, graph, colors, enterTime, minDescTime, result);
      }
    }

    System.out.println(result.size());
    for (var v : result.stream().sorted().toList()) {
      System.out.println(v + 1);
    }
  }
}
