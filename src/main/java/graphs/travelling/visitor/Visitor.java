package graphs.travelling.visitor;

import graphs.presentation.Vertex;

public interface Visitor<V extends Vertex> {
  VisitState visit(V vertex);

  Object getState(V vertex);

  boolean goToNext(V from, V to);

  void update(Object state);

  void leave(V vertex);
}
