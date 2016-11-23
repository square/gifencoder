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

import java.util.HashSet;
import java.util.Set;

/**
 * Divides the color space into uniform segments, ignoring the color profile of the original image.
 * This is very fast but tends to yield poor results, so it should only be used in situations where
 * image quality is unimportant.
 */
public final class UniformQuantizer implements ColorQuantizer {
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
