package com.squareup.jgif;

import java.util.Set;

public class NearestColorDitherer implements Ditherer {
  public static final NearestColorDitherer INSTANCE = new NearestColorDitherer();

  private NearestColorDitherer() {}

  @Override public Image dither(Image image, Set<Color> newColors) {
    int width = image.getWidth(), height = image.getHeight();
    Color[][] colors = new Color[height][width];
    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        colors[y][x] = image.getColor(x, y).getNearestColor(newColors);
      }
    }
    return new Image(colors);
  }
}
