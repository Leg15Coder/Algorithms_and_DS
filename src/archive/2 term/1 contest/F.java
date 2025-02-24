import static java.util.Collections.sort;

import java.util.*;

public class Main {
  private static List<List<Pair>> graph;
  private static short[] enterTime;
  private static short[] minDescTime;
  private static final List<Integer> result = new ArrayList<>();

  public record Pair(short vertex, int edgeIndex) {
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Pair pair)) return false;
      return vertex == pair.vertex && edgeIndex == pair.edgeIndex;
    }

    @Override
    public int hashCode() {
      return Objects.hash(vertex, edgeIndex);
    }
  }

  private static short globalTime = 0;

  public static void dfs(short current, Pair parent) {
    enterTime[current] = ++globalTime;
    minDescTime[current] = globalTime;

    for (var p : graph.get(current)) {
      short v = p.vertex;
      if (!p.equals(parent)) {
        if (enterTime[v] == 0) {
          dfs(v, new Pair(current, p.edgeIndex));
          minDescTime[current] = (short) Math.min(minDescTime[current], minDescTime[v]);

          if (minDescTime[v] > enterTime[current]) {
            result.add(p.edgeIndex);
          }
        } else {
          minDescTime[current] = (short) Math.min(minDescTime[current], enterTime[v]);
        }
      }
    }
  }

  public static void run() {
    Scanner input = new Scanner(System.in);

    int vertexCount = input.nextInt();
    int edgeCount = input.nextInt();

    enterTime = new short[vertexCount];
    minDescTime = new short[vertexCount];
    graph = new ArrayList<>(vertexCount);

    for (int i = 0; i < vertexCount; ++i) {
      graph.add(new ArrayList<>());
    }

    for (int i = 0; i < edgeCount; ++i) {
      short from = (short) (input.nextShort() - 1);
      short to = (short) (input.nextShort() - 1);

      graph.get(from).add(new Pair(to, i + 1));
      graph.get(to).add(new Pair(from, i + 1));
    }

    for (int i = 0; i < vertexCount; ++i) {
      if (enterTime[i] == 0) {
        dfs((short) i, new Pair((short) -1, -1));
      }
    }

    System.out.println(result.size());

    sort(result);

    for (var e : result) {
      System.out.println(e);
    }
  }

  public static void main(String[] args) {
    Thread thread = new Thread(null, Main::run, "big-stack-thread", 256 * 1024 * 1024);
    thread.start();
  }
}
