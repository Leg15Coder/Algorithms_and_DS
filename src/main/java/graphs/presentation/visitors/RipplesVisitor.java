package graphs.presentation.visitors;

import graphs.presentation.Vertex;
import structures.common.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RipplesVisitor<V extends Vertex> extends ColoringVisitor<V> {
  private final Set<V> used;

  public RipplesVisitor(Set<V> used) {
    this.used = used;
  }

  @Override
  public VisitState visit(V vertex) {
    int vertexState = (int) getState(vertex);

    if (vertexState == visitColor) {
      return VisitState.CYCLE;
    } else if (vertexState == leaveColor || used.contains(vertex)) {
      return VisitState.INNOCENT;
    }

    colors.put(vertex, visitColor);
    return VisitState.OK;
  }
}
