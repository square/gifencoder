package com.squareup.gifencoder;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LzwEncoderTest {
  @Test public void testGetMinimumCodeSize() {
    assertThat(new LzwEncoder(1).getMinimumCodeSize()).isEqualTo(2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidColorTableSize() {
    new LzwEncoder(7);
  }
}
