package graphs.presentation.factory;

import graphs.presentation.Edge;
import graphs.presentation.Vertex;

public class BasicEdge<V extends Vertex> implements Edge<V> {
  protected final V from;
  protected final V to;

  public BasicEdge(V from, V to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public V from() {
    return from;
  }

  @Override
  public V to() {
    return to;
  }

  @Override
  public Edge<V> reverseArguments() {
    return new BasicEdge<>(to, from);
  }
}
