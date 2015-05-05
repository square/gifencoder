package com.squareup.gifencoder;

import java.util.Arrays;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MedianCutQuantizerTest {
  @Test public void testQuantize() {
    // The red component has the greatest spread, so the original four colors should be divided into
    // two groups based on their red components.
    HashMultiset<Color> originalColors = new HashMultiset<>(Arrays.asList(
        new Color(0, 0, 0),
        new Color(1, 0, 0),
        new Color(0, 0.5, 0),
        new Color(1, 0.5, 0)));
    assertThat(MedianCutQuantizer.INSTANCE.quantize(originalColors, 2))
        .hasSize(2)
        .contains(new Color(0, 0.25, 0), new Color(1, 0.25, 0));
  }
}