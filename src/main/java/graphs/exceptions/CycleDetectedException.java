package graphs.exceptions;

public class CycleDetectedException extends GraphException {
  public CycleDetectedException(String message) {
    super(message);
  }
}
