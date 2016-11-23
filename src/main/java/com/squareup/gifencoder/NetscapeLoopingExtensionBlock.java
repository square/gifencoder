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

final class NetscapeLoopingExtensionBlock {
  private static final int EXTENSION_INTRODUCER = 0x21;
  private static final int APPLICATION_EXTENSION = 0xFF;
  private static final String APPLICATION = "NETSCAPE2.0";
  private static final int SUB_BLOCK_SIZE = 3;
  private static final int SUB_BLOCK_ID = 1;
  private static final int BLOCK_TERMINATOR = 0;

  private NetscapeLoopingExtensionBlock() {
  }

  static void write(OutputStream outputStream, int loopCount) throws IOException {
    outputStream.write(EXTENSION_INTRODUCER);
    outputStream.write(APPLICATION_EXTENSION);
    outputStream.write(APPLICATION.length());
    Streams.writeAsciiString(outputStream, APPLICATION);
    outputStream.write(SUB_BLOCK_SIZE);
    outputStream.write(SUB_BLOCK_ID);
    Streams.writeShort(outputStream, loopCount);
    outputStream.write(BLOCK_TERMINATOR);
  }
}
