package com.squareup.jgif;

import java.util.Set;

public interface Ditherer {
  Image dither(Image image, Set<Color> newColors);
}
