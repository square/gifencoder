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
import java.util.Set;

public final class GifEncoder {
  private static final int MAX_COLOR_COUNT = 256;

  private final OutputStream outputStream;
  private final int screenWidth, screenHeight;

  /**
   * Start creating a GIF file.
   *
   * @param outputStream the output stream to which the GIF data will be written
   * @param screenWidth the width of the entire graphic
   * @param screenHeight the height of the entire graphic
   * @param loopCount how many times to repeat the animation; use 0 to loop indefinitely
   * @throws IOException if there was a problem writing to the given output stream
   */
  public GifEncoder(OutputStream outputStream, int screenWidth, int screenHeight, int loopCount)
      throws IOException {
    this.outputStream = outputStream;
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
    HeaderBlock.write(outputStream);
    LogicalScreenDescriptorBlock.write(outputStream, screenWidth, screenHeight, false, 1, false, 0,
        0, 0);
    NetscapeLoopingExtensionBlock.write(outputStream, loopCount);
  }

  /**
   * Add an image to the GIF file.
   *
   * @param rgbData a grid of pixels in RGB format
   * @param options options to be applied to this image
   * @return this instance for chaining
   * @throws IOException if there was a problem writing to the given output stream
   */
  public GifEncoder addImage(int[][] rgbData, ImageOptions options) throws IOException {
    addImage(Image.fromRgb(rgbData), options);
    return this;
  }

  /**
   * Add an image to the GIF file.
   *
   * @param rgbData an image buffer in RGB format
   * @param width the number of pixels per row in the pixel array
   * @param options options to be applied to this image
   * @return this instance for chaining
   * @throws IOException if there was a problem writing to the given output stream
   */
  public GifEncoder addImage(int[] rgbData, int width, ImageOptions options) throws IOException {
    addImage(Image.fromRgb(rgbData, width), options);
    return this;
  }

  /**
   * Writes the trailer. This should be called exactly once per GIF file.
   *
   * <p>This method does not close the input stream. We consider it the caller's responsibility to
   * close it at the appropriate time, which often (but not always) will be just after calling this
   * method.
   */
  public synchronized void finishEncoding() throws IOException {
    // The trailer block indicates when you've hit the end of the file.
    outputStream.write(0x3B);
  }

  public synchronized void addImage(Image image, ImageOptions options) throws IOException {
    if (options.left + image.getWidth() > screenWidth
        || options.top + image.getHeight() > screenHeight) {
      throw new IllegalArgumentException("Image does not fit in screen.");
    }

    Multiset<Color> originalColors = image.getColors();
    Set<Color> distinctColors = originalColors.getDistinctElements();
    if (distinctColors.size() > MAX_COLOR_COUNT) {
      distinctColors = options.quantizer.quantize(originalColors, MAX_COLOR_COUNT);
      image = options.ditherer.dither(image, distinctColors);
    }

    ColorTable colorTable = ColorTable.fromColors(distinctColors);
    int paddedColorCount = colorTable.paddedSize();
    int[] colorIndices = colorTable.getIndices(image);

    GraphicsControlExtensionBlock.write(outputStream, options.disposalMethod, false, false,
        options.delayCentiseconds, 0);
    ImageDescriptorBlock.write(outputStream, options.left, options.top, image.getWidth(),
        image.getHeight(), true, false, false, getColorTableSizeField(paddedColorCount));
    colorTable.write(outputStream);

    LzwEncoder lzwEncoder = new LzwEncoder(paddedColorCount);
    byte[] lzwData = lzwEncoder.encode(colorIndices);
    ImageDataBlock.write(outputStream, lzwEncoder.getMinimumCodeSize(), lzwData);
  }

  /**
   * Compute the "size of the color table" field as the spec defines it:
   *
   * <blockquote>this field is used to calculate the number of bytes contained in the Global Color
   * Table. To determine that actual size of the color table, raise 2 to [the value of the field +
   * 1]</blockquote>
   */
  private static int getColorTableSizeField(int actualTableSize) {
    int size = 0;
    while (1 << (size + 1) < actualTableSize) {
      ++size;
    }
    return size;
  }
}
