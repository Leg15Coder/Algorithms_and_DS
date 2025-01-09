package structures.trees.segmentation.funcs;

import structures.trees.segmentation.Operation;

public class MulWithMod extends Mul {
  private final Long MOD;

  public MulWithMod(long mod) {
    this.MOD = mod;
  }

  @Override
  public Long func(Long left, Long right) {
    return super.func(left, right) % MOD;
  }
}
