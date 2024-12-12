package structures.hash;

import structures.arrays.SimplyConnectedArray;
import utils.hash.HashInterface;

public class HashSet<T> {
  protected final Bucket<T>[] buckets;
  protected final HashInterface<T> hashing;

  private static class Bucket<T> {
    private final SimplyConnectedArray<T> list = new SimplyConnectedArray<>();

    public void addElement(T element) {
      this.list.insert(element);
    }

    public int getSize() {
      return this.list.getSize();
    }
  }

  public HashSet(HashInterface<T> hashing) {
    this.buckets = new Bucket[hashing.maxValue() + 1];
    for (int i = 0; i < hashing.maxValue() + 1; ++i) {
      this.buckets[i] = new Bucket<>();
    }
    this.hashing = hashing;
  }

  public void insert(T value) {
    int h = hashing.hash(value);
    if (!get(value)) {
      buckets[h].addElement(value);
    }
  }

  public boolean get(T value) {
    int h = hashing.hash(value);
    for (int i = 0; i < buckets[h].getSize(); ++i) {
      var current = buckets[h].list.getAt(i);
      if (current.equals(value)) {
        return true;
      }
    }
    return false;
  }

  public boolean remove(T value) {
    int h = hashing.hash(value);
    if (get(value)) {
      buckets[h].list.remove(value);
      return true;
    }
    return false;
  }
}
