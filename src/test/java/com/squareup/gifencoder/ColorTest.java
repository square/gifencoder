package com.squareup.gifencoder;

import java.util.Arrays;
import java.util.Collections;
import org.assertj.core.data.Offset;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.squareup.gifencoder.ColorAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

public class ColorTest {
  private static final Offset<Double> EPSILON = Offset.offset(1e-6);

  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Test public void testFromRgbInt() {
    assertThat(Color.fromRgbInt(0)).isEqualTo(Color.BLACK, EPSILON);
    assertThat(Color.fromRgbInt(0xFFFFFF)).isEqualTo(Color.WHITE, EPSILON);
    assertThat(Color.fromRgbInt(0xFF0000)).isEqualTo(Color.RED, EPSILON);
    assertThat(Color.fromRgbInt(0x00FF00)).isEqualTo(Color.GREEN, EPSILON);
    assertThat(Color.fromRgbInt(0x0000FF)).isEqualTo(Color.BLUE, EPSILON);
  }

  @Test public void testGetCentroid() {
    assertThat(Color.getCentroid(new HashMultiset<>(Collections.singletonList(Color.BLACK))))
        .isEqualTo(Color.BLACK, EPSILON);
    assertThat(Color.getCentroid(new HashMultiset<>(Collections.singletonList(Color.WHITE))))
        .isEqualTo(Color.WHITE, EPSILON);
    assertThat(
        Color.getCentroid(new HashMultiset<>(Arrays.asList(Color.RED, Color.GREEN, Color.BLACK))))
        .isEqualTo(new Color(1.0 / 3.0, 1.0 / 3.0, 0), EPSILON);
  }

  @Test public void testGetComponent() {
    Color color = new Color(1, 2, 3);
    assertThat(color.getComponent(0)).isEqualTo(1);
    assertThat(color.getComponent(1)).isEqualTo(2);
    assertThat(color.getComponent(2)).isEqualTo(3);
  }

  @Test public void testGetComponent_negative() {
    expectedException.expect(IllegalArgumentException.class);
    Color.BLACK.getComponent(-1);
  }

  @Test public void testGetComponent_tooLarge() {
    expectedException.expect(IllegalArgumentException.class);
    Color.BLACK.getComponent(3);
  }

  @Test public void testScaled() {
    assertThat(Color.BLACK.scaled(100)).isEqualTo(Color.BLACK, EPSILON);
    assertThat(new Color(1, 2, 3).scaled(2)).isEqualTo(new Color(2, 4, 6), EPSILON);
    assertThat(new Color(1, 2, 3).scaled(10)).isEqualTo(new Color(10, 20, 30), EPSILON);
    assertThat(Color.RED.scaled(0.5)).isEqualTo(new Color(0.5, 0, 0), EPSILON);
  }

  @Test public void testPlus() {
    assertThat(Color.BLACK.plus(Color.BLACK)).isEqualTo(Color.BLACK, EPSILON);
    assertThat(new Color(1, 2, 3).plus(new Color(4, 5, 6)))
        .isEqualTo(new Color(5, 7, 9), EPSILON);
  }

  @Test public void testMinus() {
    assertThat(Color.BLACK.minus(Color.BLACK)).isEqualTo(Color.BLACK, EPSILON);
    assertThat(new Color(4, 5, 6).minus(new Color(1, 2, 3)))
        .isEqualTo(new Color(3, 3, 3), EPSILON);
    assertThat(new Color(1, 2, 3).minus(new Color(4, 5, 6)))
        .isEqualTo(new Color(-3, -3, -3), EPSILON);
  }

  @Test public void testGetEuclideanDistanceTo() {
    assertThat(Color.BLACK.getEuclideanDistanceTo(Color.BLACK)).isEqualTo(0, EPSILON);
    assertThat(Color.BLACK.getEuclideanDistanceTo(Color.RED)).isEqualTo(1, EPSILON);
    assertThat(Color.GREEN.getEuclideanDistanceTo(Color.BLUE)).isEqualTo(Math.sqrt(2), EPSILON);
    assertThat(Color.BLACK.getEuclideanDistanceTo(Color.WHITE)).isEqualTo(Math.sqrt(3), EPSILON);
  }

  @Test public void testGetNearestColor() {
    assertThat(Color.BLACK.getNearestColor(Collections.singletonList(Color.BLACK)))
        .isEqualTo(Color.BLACK, EPSILON);
    assertThat(Color.BLACK.getNearestColor(Collections.singletonList(Color.WHITE)))
        .isEqualTo(Color.WHITE, EPSILON);
    assertThat(Color.BLACK.getNearestColor(Arrays.asList(Color.BLACK, Color.WHITE)))
        .isEqualTo(Color.BLACK, EPSILON);
  }

  @Test public void testGetRgbInt() {
    assertThat(Color.BLACK.getRgbInt()).isEqualTo(0);
    assertThat(Color.WHITE.getRgbInt()).isEqualTo(0xFFFFFF);
    assertThat(Color.RED.getRgbInt()).isEqualTo(0xFF0000);
    assertThat(Color.GREEN.getRgbInt()).isEqualTo(0x00FF00);
    assertThat(Color.BLUE.getRgbInt()).isEqualTo(0x0000FF);
  }
}