package utils.hash;

public class ModuleNumHash implements HashInterface<Number> {
  private final long MOD;

  public ModuleNumHash(long mod) {
    checkValues(mod);
    this.MOD = mod;
  }

  private static void checkValues(long mod) {
    if (mod <= 0) {
      throw new IllegalArgumentException("Модуль должен быть натуральным");
    }
  }

  @Override
  public Integer hash(Number obj) {
    return (int) (obj.longValue() % MOD);
  }

  @Override
  public boolean isInCollision(Number left, Number right) {
    return hash(left).equals(hash(right));
  }

  @Override
  public Integer minValue() {
    return 0;
  }

  @Override
  public Integer maxValue() {
    return (int) (this.MOD - 1);
  }
}
