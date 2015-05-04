package com.squareup.jgif;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
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
   * @param loopCount how many times to reapeat the animation; use 0 to loop indefinitely
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

  public GifEncoder addImage(int[][] rgbData, ImageOptions options) throws IOException {
    addImage(Image.fromRgb(rgbData), options);
    return this;
  }

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

  private synchronized void addImage(Image image, ImageOptions options) throws IOException {
    if (options.left + image.getWidth() > screenWidth
        || options.top + image.getHeight() > screenHeight) {
      throw new IllegalArgumentException("Image does not fit in screen.");
    }

    Set<Color> originalColors = getDistinctColors(image);
    if (originalColors.size() > MAX_COLOR_COUNT) {
      Set<Color> newColors = options.quantizer.quantize(originalColors, MAX_COLOR_COUNT);
      image = options.ditherer.dither(image, newColors);
    }
    ColorTable colorTable = ColorTable.fromColors(getDistinctColors(image));
    int[] colorIndices = colorTable.getIndices(image);

    GraphicsControlExtensionBlock.write(outputStream, DisposalMethod.DO_NOT_DISPOSE, false, false,
        options.delayCentiseconds, 0);
    int paddedColorCount = GifMath.roundUpToPowerOfTwo(colorTable.size());
    ImageDescriptorBlock.write(outputStream, options.left, options.top, image.getWidth(),
        image.getHeight(), true, false, false, getColorTableSizeField(paddedColorCount));
    colorTable.write(outputStream, paddedColorCount);
    byte[] lzwData = new LzwEncoder(paddedColorCount).encode(colorIndices);
    ImageDataBlock.write(outputStream, LzwEncoder.getMinimumCodeSize(paddedColorCount), lzwData);
  }

  private static Set<Color> getDistinctColors(Image image) {
    Set<Color> colors = new HashSet<>();
    for (int i = 0; i < image.getNumPixels(); ++i) {
      colors.add(image.getColor(i));
    }
    return colors;
  }

  /**
   * Compute the "size of the color table" field as the spec defines it:
   *
   * <p>"this field is used to calculate the number of bytes contained in the Global Color Table. To
   * determine that actual size of the color table, raise 2 to [the value of the field + 1]
   */
  private static int getColorTableSizeField(int actualTableSize) {
    int size = 0;
    while (1 << (size + 1) < actualTableSize) {
      ++size;
    }
    return size;
  }
}
