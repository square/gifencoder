package com.squareup.gifencoder;

import java.io.IOException;
import java.io.OutputStream;

class LogicalScreenDescriptorBlock {
  private static final int GLOBAL_COLOR_TABLE_FLAG = 1 << 7;
  private static final int SORT_FLAG = 1 << 3;

  private LogicalScreenDescriptorBlock() {}

  static void write(OutputStream outputStream, int logicalScreenWidth, int logicalScreenHeight,
      boolean globalColorTable, int colorResolution, boolean sort, int globalColorTableSize,
      int backgroundColorIndex, int pixelAspectRatio) throws IOException {
    Streams.writeShort(outputStream, logicalScreenWidth);
    Streams.writeShort(outputStream, logicalScreenHeight);
    outputStream.write((globalColorTable ? GLOBAL_COLOR_TABLE_FLAG : 0)
        | (colorResolution << 4)
        | (sort ? SORT_FLAG : 0)
        | globalColorTableSize);
    outputStream.write(backgroundColorIndex);
    outputStream.write(pixelAspectRatio);
  }
}
