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

import java.util.Arrays;
import java.util.Collection;

/**
 * An RGB representation of a color, which stores each component as a double in the range [0, 1].
 * Values outside of [0, 1] are permitted though, as this is convenient e.g. for representing color
 * deltas.
 */
public final class Color {
  public static final Color BLACK = new Color(0, 0, 0);
  public static final Color WHITE = new Color(1, 1, 1);
  public static final Color RED = new Color(1, 0, 0);
  public static final Color GREEN = new Color(0, 1, 0);
  public static final Color BLUE = new Color(0, 0, 1);

  private final double red, green, blue;

  public Color(double red, double green, double blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  public static Color fromRgbInt(int rgb) {
    int redComponent = rgb >>> 16 & 0xFF;
    int greenComponent = rgb >>> 8 & 0xFF;
    int blueComponent = rgb & 0xFF;
    return new Color(redComponent / 255.0, greenComponent / 255.0, blueComponent / 255.0);
  }

  public static Color getCentroid(Multiset<Color> colors) {
    Color sum = Color.BLACK;
    for (Color color : colors.getDistinctElements()) {
      int weight = colors.count(color);
      sum = sum.plus(color.scaled(weight));
    }
    return sum.scaled(1.0 / colors.size());
  }

  public double getComponent(int index) {
    switch (index) {
      case 0:
        return red;
      case 1:
        return green;
      case 2:
        return blue;
      default:
        throw new IllegalArgumentException("Unexpected component index: " + index);
    }
  }

  public Color scaled(double s) {
    return new Color(s * red, s * green, s * blue);
  }

  public Color plus(Color that) {
    return new Color(this.red + that.red, this.green + that.green, this.blue + that.blue);
  }

  public Color minus(Color that) {
    return new Color(this.red - that.red, this.green - that.green, this.blue - that.blue);
  }

  public double getEuclideanDistanceTo(Color that) {
    Color d = this.minus(that);
    double sumOfSquares = d.red * d.red + d.green * d.green + d.blue * d.blue;
    return Math.sqrt(sumOfSquares);
  }

  /**
   * Find this color's nearest neighbor, based on Euclidean distance, among some set of colors.
   */
  public Color getNearestColor(Collection<Color> colors) {
    Color nearestCentroid = null;
    double nearestCentroidDistance = Double.POSITIVE_INFINITY;
    for (Color color : colors) {
      double distance = getEuclideanDistanceTo(color);
      if (distance < nearestCentroidDistance) {
        nearestCentroid = color;
        nearestCentroidDistance = distance;
      }
    }
    return nearestCentroid;
  }

  public int getRgbInt() {
    int redComponent = (int) (red * 255);
    int greenComponent = (int) (green * 255);
    int blueComponent = (int) (blue * 255);
    return redComponent << 16 | greenComponent << 8 | blueComponent;
  }

  @Override public boolean equals(Object o) {
    if (!(o instanceof Color)) return false;
    Color that = (Color) o;
    return this.red == that.red
        && this.green == that.green
        && this.blue == that.blue;
  }

  @Override public int hashCode() {
    return Arrays.hashCode(new double[] { red, green, blue });
  }

  @Override public String toString() {
    return String.format("Color[%f, %f, %f]", red, green, blue);
  }
}
