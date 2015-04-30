package com.squareup.jgif;

import java.util.Set;

public interface ColorQuantizer {
  /**
   * Quantizes the given set of colors, returning a set no larger than {@code maxColors}.
   *
   * <p>The intent is to pick a set of colors which are representative of the original color set,
   * but no specific guarantees are made.
   *
   * @param originalColors the full set of original colors
   * @param maxColorCount the maximum number of colors to allow
   * @return a collection of colors no larger than {@code maxColors}
   */
  Set<Color> quantize(Set<Color> originalColors, int maxColorCount);
}
