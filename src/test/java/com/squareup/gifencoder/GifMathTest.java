package com.squareup.gifencoder;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GifMathTest {
  @Test public void testRoundUpToPowerOfTwo() {
    assertThat(GifMath.roundUpToPowerOfTwo(1)).isEqualTo(1);
    assertThat(GifMath.roundUpToPowerOfTwo(2)).isEqualTo(2);
    assertThat(GifMath.roundUpToPowerOfTwo(3)).isEqualTo(4);
    assertThat(GifMath.roundUpToPowerOfTwo(4)).isEqualTo(4);
    assertThat(GifMath.roundUpToPowerOfTwo(5)).isEqualTo(8);
  }
}