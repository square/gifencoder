/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.gifencoder;

import java.util.Collection;
import java.util.Set;

/**
 * A collection which permits duplicates, and provides methods adding/removing several counts of an
 * element.
 *
 * @param <E> the element type
 */
public interface Multiset<E> extends Collection<E> {
  /**
   * Add n counts of an element.
   *
   * @param element the element to add
   * @param n how many to add
   */
  void add(E element, int n);

  /**
   * Remove up to n counts of an element.
   *
   * @param element the element the remove
   * @param n how many to remove
   * @return the number of elements removed
   */
  int remove(Object element, int n);

  int count(Object element);

  Set<E> getDistinctElements();
}
