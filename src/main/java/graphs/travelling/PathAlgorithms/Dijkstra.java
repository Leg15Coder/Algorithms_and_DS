package graphs.travelling.PathAlgorithms;

import graphs.presentation.Vertex;
import graphs.travelling.visitor.VisitState;
import graphs.travelling.visitor.Visitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dijkstra<V extends Vertex, W extends Comparable<W>> implements Visitor<V> {
  private final W kInf;
  private final Map<V, W> dp = new HashMap<>();
  private final Map<V, V> path = new HashMap<>();

  public Dijkstra(W kInf) {
    this.kInf = kInf;
  }

  @Override
  public VisitState visit(V vertex) {
    return null;
  }

  @Override
  public Object getState(V vertex) {
    return dp.getOrDefault(vertex, kInf);
  }

  @Override
  public boolean goToNext(V from, V to) {
    // return dp.getOrDefault(from, kInf);
    return false;
  }

  @Override
  public void update(Object state) {

  }

  @Override
  public void leave(V vertex) {

  }

  public List<V> getPath(V vertex) {
    return null;
  }
}
