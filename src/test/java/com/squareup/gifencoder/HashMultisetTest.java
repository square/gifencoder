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

import java.util.Arrays;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HashMultisetTest {
  @Test public void testAddAndRemove() {
    Multiset<String> multiset = new HashMultiset<>();

    multiset.add("a");
    assertThat(multiset.count("a")).isEqualTo(1);
    multiset.add("a", 3);
    assertThat(multiset.count("a")).isEqualTo(4);

    multiset.remove("a");
    assertThat(multiset.count("a")).isEqualTo(3);
    multiset.remove("a", 2);
    assertThat(multiset.count("a")).isEqualTo(1);
    multiset.remove("a", 3);
    assertThat(multiset.count("a")).isEqualTo(0);
  }

  @Test public void testSize() {
    assertThat(multiset()).hasSize(0);
    assertThat(multiset("a")).hasSize(1);
    assertThat(multiset("a", "b")).hasSize(2);
    assertThat(multiset("a", "b", "c")).hasSize(3);
  }

  private static <E> Multiset<E> multiset(E... elements) {
    return new HashMultiset<>(Arrays.asList(elements));
  }
}
