package graphs.presentation.visitors;

import graphs.presentation.Vertex;

import java.util.HashMap;
import java.util.Map;

public class ColoringVisitor<V extends Vertex> implements Visitor<V> {
  private final Map<V, Integer> colors = new HashMap<>();

  @Override
  public int visit(V vertex) {
    return colors.getOrDefault(vertex, 0);
  }

  @Override
  public int getState(V vertex) {
    return colors.getOrDefault(vertex, 0);
  }

  @Override
  public void update(V vertex, int state) {
    colors.put(vertex, state);
  }

  @Override
  public int leave(V vertex, int state) {
    return colors.put(vertex, state);
  }
}
