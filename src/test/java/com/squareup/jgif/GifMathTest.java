package com.squareup.jgif;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GifMathTest {
  @Test public void testRoundUpToPowerOfTwo() {
    assertEquals(1, GifMath.roundUpToPowerOfTwo(1));
    assertEquals(2, GifMath.roundUpToPowerOfTwo(2));
    assertEquals(4, GifMath.roundUpToPowerOfTwo(3));
    assertEquals(4, GifMath.roundUpToPowerOfTwo(4));
    assertEquals(8, GifMath.roundUpToPowerOfTwo(5));
  }
}