package com.squareup.jgif;

import java.io.IOException;
import java.io.OutputStream;

final class ImageDescriptorBlock {
  private static final int IMAGE_SEPARATOR = 0x2C;

  private static final int LOCAL_COLOR_TABLE_FLAG = 1 << 7;
  private static final int INTERLACE_FLAG = 1 << 6;
  private static final int SORT_FLAG = 1 << 5;

  private ImageDescriptorBlock() {}

  static void write(OutputStream outputStream, int imageLeft, int imageTop, int imageWidth,
      int imageHeight, boolean localColorTable, boolean interlace, boolean sort,
      int localColorTableSize) throws IOException {
    outputStream.write(IMAGE_SEPARATOR);
    Streams.writeShort(outputStream, imageLeft);
    Streams.writeShort(outputStream, imageTop);
    Streams.writeShort(outputStream, imageWidth);
    Streams.writeShort(outputStream, imageHeight);

    // Packed fields.
    outputStream.write((localColorTable ? LOCAL_COLOR_TABLE_FLAG : 0)
        | (interlace ? INTERLACE_FLAG : 0)
        | (sort ? SORT_FLAG : 0)
        | localColorTableSize);
  }
}
