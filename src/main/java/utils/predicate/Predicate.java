package utils.predicate;

import java.util.ArrayList;
import java.util.List;

import static utils.Compare.min;

public interface Predicate {
  List<Boolean> table = new ArrayList<>();

  default int boolListToInt(List<Boolean> vector) {
    int size = vector.size();
    int result = 0;

    for (int i = 0; i < min(vector.size(), table.size()); ++i) {
      result += vector.get(i) ? 1 << --size : 0;
    }

    return result;
  }

  default boolean calculate(List<Boolean> vector) {
    return table.get(boolListToInt(vector));
  }
}
