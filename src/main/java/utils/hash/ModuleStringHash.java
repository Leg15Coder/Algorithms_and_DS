package utils.hash;

public class ModuleStringHash implements HashInterface<String> {
  private final long MOD;
  private final int BASE;
  private final long MIN;

  public ModuleStringHash(long mod, int base) {
    checkValues(mod, base);
    this.MOD = mod;
    this.BASE = base;
    this.MIN = 0L;
  }

  public ModuleStringHash(long mod, int base, char min) {
    checkValues(mod, base);
    this.MOD = mod;
    this.BASE = base;
    this.MIN = min;
  }

  private static void checkValues(long mod, long base) {
    if (mod <= 0) {
      throw new IllegalArgumentException("Модуль должен быть натуральным");
    }
    if (base >= mod) {
      throw new IllegalArgumentException("Основание хеша не может быть больше или равно модулю хеша");
    }
  }

  @Override
  public Integer hash(String obj) {
    int h = 0;

    for (int i = 0; i < obj.length(); ++i) {
      h *= BASE;
      h += (int) (obj.charAt(i) - MIN);
      h %= (int) MOD;
    }

    return h;
  }

  @Override
  public boolean isInCollision(String left, String right) {
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
