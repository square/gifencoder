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

final class ImageDescriptorBlock {
  private static final int IMAGE_SEPARATOR = 0x2C;

  private static final int LOCAL_COLOR_TABLE_FLAG = 1 << 7;
  private static final int INTERLACE_FLAG = 1 << 6;
  private static final int SORT_FLAG = 1 << 5;

  private ImageDescriptorBlock() {
  }

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
