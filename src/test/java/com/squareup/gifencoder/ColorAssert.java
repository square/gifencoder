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
