package utils.hash;

public class ModuleHash implements HashInterface<String> {
  private final long MOD;
  private final long BASE;
  private final long MIN;

  public ModuleHash(long mod, long base) {
    checkValues(mod, base);
    this.MOD = mod;
    this.BASE = base;
    this.MIN = 0L;
  }

  public ModuleHash(long mod, long base, char min) {
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
  public Long hash(String obj) {
    long h = 0L;

    for (int i = 0; i < obj.length(); ++i) {
      h *= BASE;
      h += obj.charAt(i) - MIN;
      h %= MOD;
    }

    return h;
  }

  @Override
  public boolean isInCollision(String left, String right) {
    return hash(left).equals(hash(right));
  }
}
