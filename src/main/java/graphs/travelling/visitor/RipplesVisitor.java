package graphs.travelling.visitor;

import graphs.presentation.Vertex;

import java.util.Set;

public class RipplesVisitor<V extends Vertex> extends NumeratedVisitor<V> {
  private final Set<V> used;

  public RipplesVisitor(Set<V> used) {
    this.used = used;
  }

  @Override
  public VisitState visit(V vertex) {
    int vertexState = (int) getState(vertex);

    // Пропускаем уже использованные вершины
    if (vertexState == visitNumber) {
      return VisitState.INCORRECT;
    } else if (vertexState == leaveNumber || used.contains(vertex)) {
      return VisitState.INNOCENT;
    }

    numbers.put(vertex, visitNumber);
    return VisitState.OK;
  }
}
