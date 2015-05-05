package com.squareup.gifencoder;

import java.io.IOException;
import java.io.OutputStream;

class GraphicsControlExtensionBlock {
  private static final int USER_INPUT_FLAG = 1 << 1;
  private static final int TRANSPARENT_COLOR_FLAG = 1;

  private static final int EXTENSION_INTRODUCER = 0x21;
  private static final int GRAPHICS_CONTROL_LABEL = 0xF9;
  private static final int GRAPHICS_CONTROL_EXTENSION_BLOCK_SIZE = 4;
  private static final int BLOCK_TERMINATOR = 0;

  private GraphicsControlExtensionBlock() {}

  static void write(OutputStream outputStream, DisposalMethod disposalMethod, boolean userInput,
      boolean transparentColor, int delayCentiseconds, int transparentColorIndex) throws IOException {
    outputStream.write(EXTENSION_INTRODUCER);
    outputStream.write(GRAPHICS_CONTROL_LABEL);
    outputStream.write(GRAPHICS_CONTROL_EXTENSION_BLOCK_SIZE);

    // Packed field.
    outputStream.write(disposalMethod.ordinal() << 3
        | (userInput ? USER_INPUT_FLAG : 0)
        | (transparentColor ? TRANSPARENT_COLOR_FLAG : 0));

    Streams.writeShort(outputStream, delayCentiseconds);
    outputStream.write(transparentColorIndex);
    outputStream.write(BLOCK_TERMINATOR);
  }
}
