package graphs.presentation.factories;

import graphs.presentation.BasicEdge;
import graphs.presentation.BasicVertex;

public class BasicGraphFactory implements GraphComponentsFactory<BasicVertex, BasicEdge> {
  @Override
  public BasicVertex createVertex(int index) {
    return new BasicVertex(index);
  }

  @Override
  public BasicEdge createEdge(BasicVertex start, BasicVertex end) {
    return new BasicEdge(start, end);
  }
}
