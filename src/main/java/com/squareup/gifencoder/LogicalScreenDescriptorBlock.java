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

final class LogicalScreenDescriptorBlock {
  private static final int GLOBAL_COLOR_TABLE_FLAG = 1 << 7;
  private static final int SORT_FLAG = 1 << 3;

  private LogicalScreenDescriptorBlock() {
  }

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
