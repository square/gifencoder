package com.squareup.gifencoder;

import java.util.Arrays;
import java.util.HashSet;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ColorTableTest {
  @Test
  public void paddedSize_withSingleColor() {
    ColorTable table1 = createColorTable(Color.BLACK);
    ColorTable table2 = createColorTable(Color.BLACK, Color.WHITE);
    ColorTable table3 = createColorTable(Color.BLACK, Color.WHITE, Color.RED);
    ColorTable table4 = createColorTable(Color.BLACK, Color.WHITE, Color.RED, Color.GREEN);
    ColorTable table5 =
        createColorTable(Color.BLACK, Color.WHITE, Color.RED, Color.GREEN, Color.BLUE);

    // paddedSize should generally give the nearest power of 2. However, the table with a single
    // color should have a padded size of 2, because a size of 1 cannot be represented in the GIF
    // format, which uses a 2^(n + 1) encoding.
    assertThat(table1.paddedSize()).isEqualTo(2);
    assertThat(table2.paddedSize()).isEqualTo(2);
    assertThat(table3.paddedSize()).isEqualTo(4);
    assertThat(table4.paddedSize()).isEqualTo(4);
    assertThat(table5.paddedSize()).isEqualTo(8);
  }

  private ColorTable createColorTable(Color... colors) {
    return ColorTable.fromColors(new HashSet<>(Arrays.asList(colors)));
  }
}
