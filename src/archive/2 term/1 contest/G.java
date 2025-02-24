import java.util.*;

public class Main {
  public static int globalTime = 0;

  static List<List<Integer>> graph = new ArrayList<>();
  static List<Integer> enterTime = new ArrayList<>();
  static List<Integer> minDescTime = new ArrayList<>();
  static Map<Integer, List<Integer>> result = new HashMap<>();

  public static <T extends Comparable<T>> T min(T l, T r) {
    if (l == null) {
      return r;
    }
    if (r == null) {
      return l;
    }
    return (l.compareTo(r) > 0) ? r : l;
  }

  public static void dfs(int current, int parent, Set<Integer> colors) {
    colors.add(current);
    enterTime.set(current, ++globalTime);
    minDescTime.set(current, min(minDescTime.get(current), globalTime));
    int unUniteNeighbours = 0;

    for (var v : graph.get(current)) {
      if (v != parent) {
        minDescTime.set(current, min(minDescTime.get(current), enterTime.get(v)));

        if (!colors.contains(v)) {
          ++unUniteNeighbours;
          dfs(v, current, colors);
          minDescTime.set(current, min(minDescTime.get(current), minDescTime.get(v)));

          if (parent >= 0 && minDescTime.get(v) > enterTime.get(current)) {
            result.put(current, new ArrayList<>());
          }
        }
      }
    }

    if (parent < 0 && unUniteNeighbours > 1) {
      result.put(current, new ArrayList<>());
    }
  }

  public static void buildClusters(int current, Set<Integer> colors, int clusterNumber) {

    colors.add(current);

    for (var v : graph.get(current)) {
      if (!colors.contains(v)) {
        if (result.containsKey(v)) {
          result.get(v).add(clusterNumber);
        } else {
          buildClusters(v, colors, clusterNumber);
        }
      }
    }
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    int vertexCount = input.nextInt();
    int edgeCount = input.nextInt();

    Set<Integer> colors = new TreeSet<>();

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
        dfs(i, -1, colors);
      }
    }

    colors = new TreeSet<>();
    List<Integer> clusters = new ArrayList<>();
    int clusterNumber = 0;

    for (int i = 0; i < vertexCount; ++i) {
      if (!colors.contains(i) && !result.containsKey(i)) {
        buildClusters(i, colors, clusterNumber++);
        clusters.add(0);
      }
    }

    int leafsCount = 0;

    for (var v : result.keySet()) {
      for (var c : result.get(v)) {
        clusters.set(c, clusters.get(c) + 1);
      }
    }

    for (var c : clusters) {
      if (c < 2) {
        leafsCount++;
      }
    }

    if (result.isEmpty()) {
      System.out.println(0);
    } else {
      System.out.println((leafsCount + 1) / 2);
    }
  }
}
