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
    assertThat(multiset("a", "b")).hasSize(1);
    assertThat(multiset("a", "b", "c")).hasSize(3);
  }

  private static <E> Multiset<E> multiset(E... elements) {
    return new HashMultiset<>(Arrays.asList(elements));
  }
}