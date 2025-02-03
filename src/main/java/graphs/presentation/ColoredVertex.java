package graphs.presentation;

public class ColoredVertex implements Vertex {
  private final int index;
  private int color = 0;

  public ColoredVertex(int index) {
    this.index = index;
  }

  @Override
  public int getColor() {
    return 0; // todo
  }

  @Override
  public void setColor() {
    // todo
  }

  @Override
  public int getIndex() {
    return index;
  }
}
