package com.squareup.jgif;

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
  public ImageOptions() {}

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
