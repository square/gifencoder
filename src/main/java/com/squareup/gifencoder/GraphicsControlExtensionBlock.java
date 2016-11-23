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

final class GraphicsControlExtensionBlock {
  private static final int USER_INPUT_FLAG = 1 << 1;
  private static final int TRANSPARENT_COLOR_FLAG = 1;

  private static final int EXTENSION_INTRODUCER = 0x21;
  private static final int GRAPHICS_CONTROL_LABEL = 0xF9;
  private static final int GRAPHICS_CONTROL_EXTENSION_BLOCK_SIZE = 4;
  private static final int BLOCK_TERMINATOR = 0;

  private GraphicsControlExtensionBlock() {
  }

  static void write(OutputStream outputStream, DisposalMethod disposalMethod, boolean userInput,
      boolean transparentColor, int delayCentiseconds, int transparentColorIndex)
      throws IOException {
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
