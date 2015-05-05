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
final class ArbitraryComparator implements Comparator<Object> {
  public static final ArbitraryComparator INSTANCE = new ArbitraryComparator();

  /**
   * If we have no other way to order two objects in a stable manner, we will register both in this
   * map and order them according to their associated values. The map's values are just integers
   * corresponding to the order in which objects were added.
   */
  private static final WeakHashMap<Object, Integer> objectIds = new WeakHashMap<>();

  private ArbitraryComparator() {
  }

  @Override public int compare(Object a, Object b) {
    if (a == b) return 0;
    if (a == null) return -1;
    if (b == null) return 1;

    int identityHashCodeDifference = System.identityHashCode(a) - System.identityHashCode(b);
    if (identityHashCodeDifference != 0) {
      return identityHashCodeDifference;
    }

    // We have an identityHashCode collision.
    return getObjectId(a) - getObjectId(b);
  }

  /**
   * Get the ID of an object, adding it to the ID map if it isn't already registered.
   */
  private static int getObjectId(Object object) {
    synchronized (objectIds) {
      Integer id = objectIds.get(object);
      if (id == null) {
        id = objectIds.size();
        objectIds.put(object, id);
      }
      return id;
    }
  }
}
