package com.squareup.gifencoder;

import java.util.HashSet;
import java.util.Set;

/**
 * Divides the color space into uniform segments, ignoring the color profile of the original image.
 * This is very fast but tends to yield poor results, so it should only be used in situations where
 * image quality is unimportant.
 */
final class UniformQuantizer implements ColorQuantizer {
  public static final UniformQuantizer INSTANCE = new UniformQuantizer();

  private UniformQuantizer() {
  }

  @Override public Set<Color> quantize(Multiset<Color> originalColors, int maxColorCount) {
    int baseSegments = (int) Math.pow(maxColorCount, 1.0 / 3.0);
    int redSegments = baseSegments, greenSegments = baseSegments, blueSegments = baseSegments;

    // See if we can add an extra segment to one or two channels.
    if (redSegments * (greenSegments + 1) * blueSegments <= maxColorCount) {
      ++greenSegments;
    }
    if ((redSegments + 1) * greenSegments * blueSegments <= maxColorCount) {
      ++redSegments;
    }

    Set<Color> colors = new HashSet<>();
    for (int redSegment = 0; redSegment < redSegments; ++redSegment) {
      for (int greenSegment = 0; greenSegment < greenSegments; ++greenSegment) {
        for (int blueSegment = 0; blueSegment < blueSegments; ++blueSegment) {
          double r = redSegment / (redSegments - 1.0);
          double g = greenSegment / (greenSegments - 1.0);
          double b = blueSegment / (blueSegments - 1.0);
          colors.add(new Color(r, g, b));
        }
      }
    }
    return colors;
  }
}
