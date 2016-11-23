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

import java.util.Arrays;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MedianCutQuantizerTest {
  @Test public void testQuantize() {
    // The red component has the greatest spread, so the original four colors should be divided into
    // two groups based on their red components.
    HashMultiset<Color> originalColors = new HashMultiset<>(Arrays.asList(
        new Color(0, 0, 0),
        new Color(1, 0, 0),
        new Color(0, 0.5, 0),
        new Color(1, 0.5, 0)));
    assertThat(MedianCutQuantizer.INSTANCE.quantize(originalColors, 2))
        .hasSize(2)
        .contains(new Color(0, 0.25, 0), new Color(1, 0.25, 0));
  }
}
