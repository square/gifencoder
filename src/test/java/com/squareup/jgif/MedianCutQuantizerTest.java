package com.squareup.jgif;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MedianCutQuantizerTest {
  @Test public void testQuantize() {
    // The red component has the greatest spread, so the original four colors should be split in
    // half based on their red components.
    Set<Color> originalColors = colors(
        new Color(0, 0, 0),
        new Color(1, 0, 0),
        new Color(0, 0.5, 0),
        new Color(1, 0.5, 0));
    Set<Color> expectedQuantizedColors = colors(
        new Color(0, 0.25, 0),
        new Color(1, 0.25, 0));
    assertEquals(expectedQuantizedColors, MedianCutQuantizer.INSTANCE.quantize(originalColors, 2));
  }

  private static Set<Color> colors(Color... colorArray) {
    return new HashSet<>(Arrays.asList(colorArray));
  }
}