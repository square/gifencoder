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

import java.util.Set;

/**
 * A trivial "ditherer" which always picks the closest color for each pixel, with no error
 * propagation.
 */
public final class NearestColorDitherer implements Ditherer {
  public static final NearestColorDitherer INSTANCE = new NearestColorDitherer();

  private NearestColorDitherer() {
  }

  @Override public Image dither(Image image, Set<Color> newColors) {
    int width = image.getWidth(), height = image.getHeight();
    Color[][] colors = new Color[height][width];
    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        colors[y][x] = image.getColor(x, y).getNearestColor(newColors);
      }
    }
    return Image.fromColors(colors);
  }
}
