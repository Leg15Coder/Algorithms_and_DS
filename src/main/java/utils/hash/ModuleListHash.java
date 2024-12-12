package utils.hash;

import structures.arrays.ArrayInterface;

import java.util.ArrayList;
import java.util.List;

public class ModuleListHash implements HashInterface<String> {
  private final ArrayInterface<Long> MODS;
  private final ArrayInterface<Integer> BASES;
  private final long MIN;
  private final long MAX_MOD = 1L; // todo later

  public ModuleListHash(ArrayInterface<Long> mods, ArrayInterface<Integer> bases) {
    checkValues(mods, bases);
    this.MODS = mods;
    this.BASES = bases;
    this.MIN = 0L;
    // this.MAX_MOD = max(mods);
  }

  public ModuleListHash(ArrayInterface<Long> mods, ArrayInterface<Integer> bases, char min) {
    checkValues(mods, bases);
    this.MODS = mods;
    this.BASES = bases;
    this.MIN = min;
  }

  private static void checkValues(ArrayInterface<Long> mods, ArrayInterface<Integer> bases) {
    if (mods.getSize() != bases.getSize()) {
      throw new IllegalArgumentException("Массивы модулей и оснований должны иметь равные длины");
    }
    if (mods.isEmpty()) {
      throw new IllegalArgumentException("Массивы модулей и оснований не могут быть пустыми");
    }
    for (int i = 0; i < mods.getSize(); ++i) {
      var mod = mods.getAt(i);
      var base = bases.getAt(i);

      if (mod <= 0) {
        throw new IllegalArgumentException("Модуль должен быть натуральным");
      }
      if (base >= mod) {
        throw new IllegalArgumentException("Основание хеша не может быть больше или равно модулю хеша");
      }
    }
  }

  public List<Integer> hashList(String obj) {
    List<Integer> result = new ArrayList<>();

    for (int j = 0; j < MODS.getSize(); ++j) {
      int h = 0;

      for (int i = 0; i < obj.length(); ++i) {
        h *= BASES.getAt(j);
        h += (int) (obj.charAt(i) - MIN);
        h %= MODS.getAt(j);
      }

      result.add(h);
    }

    return result;
  }

  @Override
  public Integer hash(String obj) {
    int h = 0;
    for (var cur : hashList(obj)) {
      h += cur;
      h %= (int) MAX_MOD;
    }

    return h;
  }

  @Override
  public boolean isInCollision(String left, String right) {
    return hashList(left).equals(hashList(right));
  }

  @Override
  public Integer minValue() {
    return 0; // todo
  }

  @Override
  public Integer maxValue() {
    return 0; // todo
  }
}
