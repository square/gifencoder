package com.squareup.jgif;

import java.io.IOException;
import java.io.OutputStream;

final class HeaderBlock {
  private HeaderBlock() {}

  static void write(OutputStream outputStream) throws IOException {
    Streams.writeAsciiString(outputStream, "GIF");
    Streams.writeAsciiString(outputStream, "89a");
  }
}
