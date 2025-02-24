import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

public class Main {
  public static int dfs(
      int start, List<Integer> graph, List<Integer> colors, List<Integer> result) {

    if (result.get(start) != null) {
      return result.get(start);
    }

    Stack<Integer> stack = new Stack<>();
    stack.push(start);
    boolean flag = true;
    int count = 0;

    while (flag) {
      int current = stack.peek();
      int bestFriend = graph.get(current);
      colors.set(current, count + 1);
      count++;

      if (result.get(bestFriend) != null) {
        result.set(current, result.get(bestFriend) + 1);
        flag = false;

      } else if (colors.get(bestFriend) > 0) {
        int cycleLength = colors.get(current) + 1 - colors.get(bestFriend);

        while (current != bestFriend) {
          result.set(current, cycleLength);
          current = stack.pop();
        }
        result.set(current, cycleLength);
        stack.push(current);
        flag = false;

      } else {
        stack.push(bestFriend);
      }
    }

    int last = stack.pop();

    while (!stack.isEmpty()) {
      int current = stack.pop();
      result.set(current, result.get(last) + 1);
      last = current;
    }

    return result.get(start);
  }

  public static void main(String[] args) throws IOException {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter out = new PrintWriter(System.out);
    StringTokenizer tokenizer = new StringTokenizer(input.readLine());

    int vertexCount = Integer.parseInt(tokenizer.nextToken());
    int operationsCount = Integer.parseInt(tokenizer.nextToken());

    List<Integer> graph = new ArrayList<>();
    List<Integer> colors = new ArrayList<>();
    List<Integer> result = new ArrayList<>();

    tokenizer = new StringTokenizer(input.readLine());
    for (int i = 0; i < vertexCount; ++i) {
      int to = Integer.parseInt(tokenizer.nextToken()) - 1;
      result.add(null);
      graph.add(to);
      colors.add(0);
    }

    for (int i = 0; i < operationsCount; ++i) {
      tokenizer = new StringTokenizer(input.readLine());
      int operation = Integer.parseInt(tokenizer.nextToken());
      int vertex = Integer.parseInt(tokenizer.nextToken()) - 1;

      if (operation == 1) {
        graph.add(vertex);
        result.add(null);
        colors.add(0);
      } else {
        out.print(dfs(vertex, graph, colors, result) + "\n");
      }
    }
    out.flush();
  }
}
