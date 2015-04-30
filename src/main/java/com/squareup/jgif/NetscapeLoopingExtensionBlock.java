package com.squareup.jgif;

import java.io.IOException;
import java.io.OutputStream;

final class NetscapeLoopingExtensionBlock {
  private static final int EXTENSION_INTRODUCER = 0x21;
  private static final int APPLICATION_EXTENSION = 0xFF;
  private static final String APPLICATION = "NETSCAPE2.0";
  private static final int BLOCK_TERMINATOR = 0;

  private NetscapeLoopingExtensionBlock() {}

  static void write(OutputStream outputStream, int repeatCount) throws IOException {
    outputStream.write(EXTENSION_INTRODUCER);
    outputStream.write(APPLICATION_EXTENSION);
    outputStream.write(APPLICATION.length());
    Streams.writeAsciiString(outputStream, APPLICATION);
    outputStream.write(3); // subblock size
    outputStream.write(1);
    Streams.writeShort(outputStream, repeatCount);
    outputStream.write(BLOCK_TERMINATOR);
  }
}
