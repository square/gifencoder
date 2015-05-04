package com.squareup.jgif;

import java.io.IOException;
import java.io.OutputStream;

class Streams {
  private Streams() {}

  static void writeShort(OutputStream outputStream, int n) throws IOException {
    // Little-endian encoded.
    outputStream.write(n);
    outputStream.write(n >>> 8);
  }

  static void writeRgb(OutputStream outputStream, int rgb) throws IOException {
    outputStream.write(rgb >>> 16); // r
    outputStream.write(rgb >>> 8); // g
    outputStream.write(rgb); // b
  }

  static void writeAsciiString(OutputStream outputStream, String string) throws IOException {
    for (char character : string.toCharArray()) {
      outputStream.write(character);
    }
  }
}
