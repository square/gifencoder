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
