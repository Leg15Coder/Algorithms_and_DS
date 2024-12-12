package structures.hash;

import utils.hash.HashInterface;

public class HashMultiSet<T> extends HashSet<T> { // todo later
  public HashMultiSet(HashInterface<T> hashing) {
    super(hashing);
  }

  @Override
  public void insert(T value) {
    int h = hashing.hash(value);
    // super.buckets[h].addElement(dp);
  }
}
