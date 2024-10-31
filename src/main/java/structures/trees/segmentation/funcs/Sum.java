package structures.trees.segmentation.funcs;

import structures.trees.segmentation.Operation;

public class Sum implements Operation<Long> {
  @Override
  public Long func(Long left, Long right) {
    return left + right;
  }

  @Override
  public Long neutral() {
    return 0L;
  }
}
