import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

public class Main {
  public static class BigUnions {
    public int first = -1;
    public int second = -1;
    public int third = -1;

    public void push(int next) {
      if (first < 0) {
        first = next;
      } else if (second < 0) {
        second = next;
      } else if (third < 0) {
        third = next;
      }
    }

    public int size() {
      return (first < 0 ? 0 : 1) + (second < 0 ? 0 : 1) + (third < 0 ? 0 : 1);
    }

    public boolean contains(int x) {
      return first == x || second == x || third == x;
    }

    @Override
    public String toString() {
      return "{" + first + ", " + second + ", " + third + '}';
    }
  }

  public record Union(int u, int v, int t) {
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Union union)) return false;
      return u == union.u && v == union.v && t == union.t
          || u == union.v && v == union.u && t == union.t
          || u == union.t && v == union.v && t == union.u
          || u == union.u && v == union.t && t == union.v
          || u == union.v && v == union.t && t == union.u
          || u == union.t && v == union.u && t == union.v;
    }

    @Override
    public int hashCode() {
      int x1 = u < v ? (Math.min(u, t)) : (Math.min(v, t));
      int x3 = u > v ? (Math.max(u, t)) : (Math.max(v, t));
      int x2 = u + v + t - x1 - x3;
      return Objects.hash(x1, x2, x3);
    }
  }

  public static int getAnyNotBigVertex(BigUnions bigUnions) {
    for (int x = 0; x < 4; ++x) {
      if (bigUnions.first != x && bigUnions.second != x && bigUnions.third != x) {
        return x;
      }
    }

    return 4;
  }

  public static void main(String[] args) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter out = new PrintWriter(System.out);
    StringTokenizer tokenizer = new StringTokenizer(reader.readLine());

    int vertexCount = Integer.parseInt(tokenizer.nextToken());
    int unionsCount = Integer.parseInt(tokenizer.nextToken());

    int[] p = new int[vertexCount];
    List<List<Integer>> adjacent = new ArrayList<>();
    Map<Union, Integer> unions = new HashMap<>();

    for (int i = 0; i < vertexCount; ++i) {
      adjacent.add(new ArrayList<>());
    }

    for (int i = 0; i < unionsCount; ++i) {
      tokenizer = new StringTokenizer(reader.readLine());
      int u = Integer.parseInt(tokenizer.nextToken()) - 1;
      int v = Integer.parseInt(tokenizer.nextToken()) - 1;
      int t = Integer.parseInt(tokenizer.nextToken()) - 1;
      int s = Integer.parseInt(tokenizer.nextToken()) - 3;

      adjacent.get(u).add(v);
      adjacent.get(u).add(t);
      adjacent.get(v).add(u);
      adjacent.get(v).add(t);
      adjacent.get(t).add(v);
      adjacent.get(t).add(u);

      unions.put(new Union(u, v, t), s);
    }

    BigUnions bigUnions = new BigUnions();

    for (int i = 0; i < vertexCount; ++i) {
      if (adjacent.get(i).size() >= vertexCount - 1) {
        BitSet unique = new BitSet(vertexCount);
        for (int x : adjacent.get(i)) {
          unique.set(x);
        }

        if (unique.cardinality() == vertexCount - 1) {
          bigUnions.push(i);

          if (bigUnions.size() == 3) {
            break;
          }
        }
      }
    }

    adjacent = new ArrayList<>();
    if (bigUnions.size() != 3) {
      out.println("Impossible");
      out.flush();
      return;
    }

    int x = getAnyNotBigVertex(bigUnions);
    int s1 =
        unions.getOrDefault(new Union(bigUnions.first, bigUnions.second, x), 0)
            - unions.getOrDefault(new Union(bigUnions.second, bigUnions.third, x), 0);
    int s2 =
        unions.getOrDefault(new Union(bigUnions.first, bigUnions.third, x), 0)
            - unions.getOrDefault(new Union(bigUnions.second, bigUnions.third, x), 0);
    int tripleBig =
        unions.getOrDefault(new Union(bigUnions.first, bigUnions.second, bigUnions.third), 0)
            + s1
            + s2;

    if (tripleBig < 0 || tripleBig % 3 != 0 || tripleBig / 3 - s2 < 0 || tripleBig / 3 - s1 < 0) {
      out.println("Impossible");
      out.flush();
      return;
    }

    Set<Integer> checkUnique = new HashSet<>();

    p[bigUnions.first] = tripleBig / 3;
    p[bigUnions.second] = tripleBig / 3 - s2;
    p[bigUnions.third] = tripleBig / 3 - s1;

    checkUnique.add(p[bigUnions.first]);
    checkUnique.add(p[bigUnions.second]);
    checkUnique.add(p[bigUnions.third]);

    for (int i = 0; i < vertexCount; ++i) {
      if (!bigUnions.contains(i)) {
        p[i] =
            unions.getOrDefault(new Union(bigUnions.first, bigUnions.second, i), 0)
                - p[bigUnions.first]
                - p[bigUnions.second];
        checkUnique.add(p[i]);

        if (p[i] < 0 || p[i] >= vertexCount) {
          out.println("Impossible");
          out.flush();
          return;
        }
      }
    }

    for (var union : unions.keySet()) {
      if (p[union.v] + p[union.u] + p[union.t] != unions.get(union)) {
        out.println("Impossible");
        out.flush();
        return;
      }
    }

    if (checkUnique.size() != vertexCount) {
      out.println("Impossible");
      out.flush();
      return;
    }

    out.println("Possible");
    for (int i = 0; i < vertexCount; ++i) {
      out.print((p[i] + 1) + " ");
    }
    out.flush();
  }
}
