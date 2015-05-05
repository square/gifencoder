package com.squareup.gifencoder;

import java.util.Comparator;
import java.util.WeakHashMap;

/**
 * Provides a stable ordering of objects, such that:
 *
 * <ul>
 *   <li>compare(a, b) == 0 iff a == b</li>
 *   <li>sign(compare(a, b)) == -sign(compare(b, a))</li>
 * </ul>
 *
 * <p>Similar to Guava's {code Ordering.arbitrary()}.
 */
class ArbitraryComparator implements Comparator<Object> {
  public static final ArbitraryComparator INSTANCE = new ArbitraryComparator();

  /**
   * A custom scheme for ordering arbitrary objects.
   */
  private static final WeakHashMap<Object, Integer> objectIndices = new WeakHashMap<>();

  private ArbitraryComparator() {}

  @Override public int compare(Object a, Object b) {
    if (a == b) return 0;
    if (a == null) return -1;
    if (b == null) return 1;

    int identityHashCodeDifference = System.identityHashCode(a) - System.identityHashCode(b);
    if (identityHashCodeDifference != 0) {
      return identityHashCodeDifference;
    }

    // We have an identityHashCode collision.
    return getObjectIndex(a) - getObjectIndex(b);
  }

  /**
   * Get the index of an object, adding it to the index map if it isn't already registered.
   */
  private static int getObjectIndex(Object object) {
    synchronized (objectIndices) {
      Integer id = objectIndices.get(object);
      if (id == null) {
        id = objectIndices.size();
        objectIndices.put(object, id);
      }
      return id;
    }
  }
}
