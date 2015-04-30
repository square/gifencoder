package com.squareup.jgif;

final class GifMath {
  private GifMath() {}

  static int roundUpToPowerOfTwo(int n) {
    n--;
    n |= n >> 1;
    n |= n >> 2;
    n |= n >> 4;
    n |= n >> 8;
    n |= n >> 16;
    n++;
    return n;
  }
}
