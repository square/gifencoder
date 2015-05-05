package com.squareup.gifencoder;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;

import static org.assertj.core.api.Assertions.assertThat;

class ColorAssert extends AbstractAssert<ColorAssert, Color> {
  ColorAssert(Color actual) {
    super(actual, ColorAssert.class);
  }

  static ColorAssert assertThat(Color actual) {
    return new ColorAssert(actual);
  }

  ColorAssert isEqualTo(Color expected, Offset<Double> offset) {
    for (int i = 0; i < 3; ++i) {
      Assertions.assertThat(actual.getComponent(i)).isEqualTo(expected.getComponent(i), offset);
    }
    return this;
  }
}
