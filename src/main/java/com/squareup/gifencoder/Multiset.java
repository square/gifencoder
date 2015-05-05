package com.squareup.gifencoder;

import java.util.Collection;
import java.util.Set;

public interface Multiset<E> extends Collection<E> {
  void add(E value, int n);

  int remove(Object value, int n);

  int count(Object element);

  Set<E> getDistinctElements();
}
