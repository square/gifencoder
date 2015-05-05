package com.squareup.gifencoder;

/**
 * An immutable grid of pixel colors.
 */
public final class Image {
  /**
   * The first index corresponds to the row, while the second index corresponds the column.
   */
  private final Color[][] colors;

  private Image(Color[][] colors) {
    this.colors = colors;
  }

  public static Image fromColors(Color[][] colors) {
    return new Image(colors);
  }

  public static Image fromRgb(int[][] rgb) {
    int height = rgb.length, width = rgb[0].length;
    Color[][] colors = new Color[height][width];
    for (int y = 0; y < height; ++y) {
      if (rgb[y].length != width) {
        throw new IllegalArgumentException("rows lengths do not match in RGB array");
      }
      for (int x = 0; x < width; ++x) {
        colors[y][x] = Color.fromRgbInt(rgb[y][x]);
      }
    }
    return new Image(colors);
  }

  public static Image fromRgb(int[] rgb, int width) {
    if (rgb.length % width != 0) {
      throw new IllegalArgumentException("the given width does not divide the number of pixels");
    }

    int height = rgb.length / width;
    Color[][] colors = new Color[height][width];
    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        colors[y][x] = Color.fromRgbInt(rgb[y * width + x]);
      }
    }
    return new Image(colors);
  }

  public Color getColor(int x, int y) {
    return colors[y][x];
  }

  public Color getColor(int index) {
    return colors[index / getWidth()][index % getWidth()];
  }

  Multiset<Color> getColors() {
    Multiset<Color> colorCounts = new HashMultiset<>();
    for (int i = 0; i < getNumPixels(); ++i) {
      Color color = getColor(i);
      colorCounts.add(color);
    }
    return colorCounts;
  }

  public int getWidth() {
    return colors[0].length;
  }

  public int getHeight() {
    return colors.length;
  }

  public int getNumPixels() {
    return getWidth() * getHeight();
  }
}
