package graphs.presentation.visitors;

import graphs.presentation.Vertex;

import java.util.HashMap;
import java.util.Map;

public class TimerVisitor<V extends Vertex> extends ColoringVisitor<V> {
  protected final Map<V, Long> tin = new HashMap<>();
  protected final Map<V, Long> tout = new HashMap<>();
  private long globalTime = 0;

  @Override
  public VisitState visit(V vertex) {
    VisitState result = super.visit(vertex);

    if (result == VisitState.OK) {
      tin.put(vertex, ++globalTime);
    }

    return result;
  }

  @Override
  public void leave(V vertex) {
    tout.put(vertex, ++globalTime);
    super.leave(vertex);
  }
}
