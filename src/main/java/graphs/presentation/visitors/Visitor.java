package graphs.presentation.visitors;

import graphs.presentation.Vertex;

public interface Visitor<V extends Vertex> {
  int visit(V vertex);

  int getState(V vertex);

  void update(V vertex, int state);

  int leave(V vertex, int state);
}
