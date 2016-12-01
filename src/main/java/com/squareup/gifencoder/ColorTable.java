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

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

final class ColorTable {
  private final Map<Integer, Color> indexToColor;
  private final Map<Color, Integer> colorToIndex;

  private ColorTable(Map<Integer, Color> indexToColor, Map<Color, Integer> colorToIndex) {
    this.indexToColor = indexToColor;
    this.colorToIndex = colorToIndex;
  }

  static ColorTable fromColors(Set<Color> colors) {
    Map<Integer, Color> indexToColor = new HashMap<>();
    Map<Color, Integer> colorToIndex = new HashMap<>();

    int index = 0;
    for (Color color : colors) {
      if (!colorToIndex.containsKey(color)) {
        indexToColor.put(index, color);
        colorToIndex.put(color, index);
        ++index;
      }
    }

    return new ColorTable(indexToColor, colorToIndex);
  }

  int paddedSize() {
    return GifMath.roundUpToPowerOfTwo(unpaddedSize());
  }

  private int unpaddedSize() {
    return colorToIndex.size();
  }

  void write(OutputStream outputStream) throws IOException {
    for (int i = 0; i < unpaddedSize(); ++i) {
      Streams.writeRgb(outputStream, indexToColor.get(i).getRgbInt());
    }
    for (int i = unpaddedSize(); i < paddedSize(); ++i) {
      Streams.writeRgb(outputStream, 0);
    }
  }

  int[] getIndices(Image image) {
    int[] result = new int[image.getNumPixels()];
    for (int i = 0; i < result.length; ++i) {
      result[i] = colorToIndex.get(image.getColor(i));
    }
    return result;
  }
}
