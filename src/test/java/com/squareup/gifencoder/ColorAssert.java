package com.squareup.gifencoder;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.data.Offset;

class ColorAssert extends AbstractAssert<ColorAssert, Color> {
  private ColorAssert(Color actual) {
    super(actual, ColorAssert.class);
  }

  static ColorAssert assertThat(Color actual) {
    return new ColorAssert(actual);
  }

  ColorAssert isEqualTo(Color expected, Offset<Double> offset) {
    for (int i = 0; i < 3; ++i) {
      if (Math.abs(expected.getComponent(i) - actual.getComponent(i)) > offset.value) {
        failWithMessage("Expected <%s> but received <%s>.", expected, actual);
      }
    }
    return this;
  }
}
