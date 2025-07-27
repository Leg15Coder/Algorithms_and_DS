package graphs.travelling.visitor;

import graphs.presentation.Vertex;
import structures.common.Pair;

import java.util.HashMap;
import java.util.Map;

public class NumeratedVisitor<V extends Vertex> implements Visitor<V> {
  protected final Map<V, Integer> numbers = new HashMap<>();
  protected int visitNumber = 0;
  protected int leaveNumber = 0;

  @Override
  public VisitState visit(V vertex) {
    int vertexState = (int) getState(vertex);

    if (vertexState == visitNumber) {
      return VisitState.INCORRECT;
    } else if (vertexState == leaveNumber) {
      return VisitState.INNOCENT;
    }

    numbers.put(vertex, visitNumber);
    return VisitState.OK;
  }

  @Override
  public Object getState(V vertex) {
    return numbers.getOrDefault(vertex, 0);
  }

  @Override
  public boolean goToNext(V from, V to) {
    return (int) getState(to) != leaveNumber;
  }

  @Override
  public void update(Object state) {
    try {
      Pair<Integer, Integer> newNumbers = (Pair<Integer, Integer>) state;
      visitNumber = newNumbers.first();
      leaveNumber = newNumbers.second();
    } catch (ClassCastException e) {
      throw new IllegalArgumentException(
          "В state необходимо передать пару числовых значений типа Pair<>");
    }
  }

  @Override
  public void leave(V vertex) {
    numbers.put(vertex, leaveNumber);
  }
}

