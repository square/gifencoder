package com.squareup.gifencoder;

import java.io.IOException;
import java.io.OutputStream;

final class ImageDataBlock {
  private ImageDataBlock() {
  }

  static void write(OutputStream outputStream, int minimumCodeSize, byte[] lzwData)
      throws IOException {
    outputStream.write(minimumCodeSize);
    int index = 0;
    while (index < lzwData.length) {
      int subBlockLength = Math.min(lzwData.length - index, 255);
      outputStream.write(subBlockLength);
      outputStream.write(lzwData, index, subBlockLength);
      index += subBlockLength;
    }
    outputStream.write(0);
  }
}
