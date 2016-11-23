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

import java.util.concurrent.TimeUnit;

public final class ImageOptions {
  int left = 0;
  int top = 0;
  ColorQuantizer quantizer = MedianCutQuantizer.INSTANCE;
  Ditherer ditherer = FloydSteinbergDitherer.INSTANCE;
  DisposalMethod disposalMethod = DisposalMethod.UNSPECIFIED;
  int delayCentiseconds = 0;

  /**
   * Create a new {@link ImageOptions} with all the defaults.
   */
  public ImageOptions() {
  }

  public ImageOptions setLeft(int left) {
    this.left = left;
    return this;
  }

  public ImageOptions setTop(int top) {
    this.top = top;
    return this;
  }

  public ImageOptions setColorQuantizer(ColorQuantizer quantizer) {
    this.quantizer = quantizer;
    return this;
  }

  public ImageOptions setDitherer(Ditherer ditherer) {
    this.ditherer = ditherer;
    return this;
  }

  public ImageOptions setDisposalMethod(DisposalMethod disposalMethod) {
    this.disposalMethod = disposalMethod;
    return this;
  }

  public ImageOptions setDelay(long duration, TimeUnit unit) {
    this.delayCentiseconds = (int) (unit.toMillis(duration) / 10);
    return this;
  }
}
