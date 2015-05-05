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

  int size() {
    return colorToIndex.size();
  }

  void write(OutputStream outputStream, int paddedColorCount) throws IOException {
    for (int i = 0; i < size(); ++i) {
      Streams.writeRgb(outputStream, indexToColor.get(i).getRgbInt());
    }
    for (int i = size(); i < paddedColorCount; ++i) {
      Streams.writeRgb(outputStream, 0);
    }
  }

  Color getColor(int index) {
    Color color = indexToColor.get(index);
    if (color == null) {
      throw new IllegalArgumentException("Index out of bounds: " + index);
    }
    return color;
  }

  int[] getIndices(Image image) {
    int[] result = new int[image.getNumPixels()];
    for (int i = 0; i < result.length; ++i) {
      result[i] = colorToIndex.get(image.getColor(i));
    }
    return result;
  }

  public int getColorCount() {
    return indexToColor.size();
  }
}
