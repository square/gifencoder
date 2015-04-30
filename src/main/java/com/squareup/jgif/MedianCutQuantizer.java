package com.squareup.jgif;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Implements median cut quantization.
 *
 * <p>The algorithm works as follows:
 *
 * <ul>
 *   <li>Begin with one cluster containing all the original colors.</li>
 *   <li>Find the cluster containing the greatest spread along a single color component (red,
 *   green or blue).</li>
 *   <li>Find the median of that color component among colors in the cluster.</li>
 *   <li>Split the cluster into two halves, using that median as a threshold.</li>
 *   <li>Repeat this process until the desired number of clusters is reached.</li>
 * </ul>
 */
public final class MedianCutQuantizer implements ColorQuantizer {
  public static final MedianCutQuantizer INSTANCE = new MedianCutQuantizer();

  private MedianCutQuantizer() {}

  @Override public Set<Color> quantize(Set<Color> originalColors, int maxColorCount) {
    TreeSet<Cluster> clusters = new TreeSet<>();
    clusters.add(new Cluster(originalColors));

    while (clusters.size() < maxColorCount) {
      Cluster clusterWithLargestSpread = clusters.pollFirst();
      clusters.addAll(clusterWithLargestSpread.split());
    }

    Set<Color> clusterCentroids = new HashSet<>();
    for (Cluster cluster : clusters) {
      clusterCentroids.add(Color.getCentroid(cluster.colors));
    }
    return clusterCentroids;
  }

  private static class Cluster implements Comparable<Cluster> {
    final Set<Color> colors;
    double largestSpread;
    int componentWithLargestSpread;

    Cluster(Set<Color> colors) {
      this.colors = colors;
      this.largestSpread = -1;
      for (int component = 0; component < 3; ++component) {
        double componentSpread = getComponentSpread(component);
        if (componentSpread > largestSpread) {
          largestSpread = componentSpread;
          componentWithLargestSpread = component;
        }
      }
    }

    double getComponentSpread(int component) {
      double min = Double.POSITIVE_INFINITY;
      double max = Double.NEGATIVE_INFINITY;
      for (Color color : colors) {
        min = Math.min(min, color.getComponent(component));
        max = Math.max(max, color.getComponent(component));
      }
      return max - min;
    }

    Collection<Cluster> split() {
      List<Color> orderedColors = new ArrayList<>(colors);
      Collections.sort(orderedColors, new ColorComponentComparator(componentWithLargestSpread));
      int medianIndex = orderedColors.size() / 2;
      return Arrays.asList(
          new Cluster(new HashSet<>(orderedColors.subList(0, medianIndex))),
          new Cluster(new HashSet<>(orderedColors.subList(medianIndex, orderedColors.size()))));
    }

    @Override public int compareTo(Cluster that) {
      double spreadDifference = that.largestSpread - this.largestSpread;
      if (spreadDifference == 0) {
        // Two different clusters might have the same largestSpread, but we don't want to return 0
        // as then TreeSet would think they're the same instance. ArbitraryComparator will return
        // something nonzero if this != that.
        return ArbitraryComparator.INSTANCE.compare(this, that);
      }
      return (int) Math.signum(spreadDifference);
    }
  }

  private static class ColorComponentComparator implements Comparator<Color> {
    final int component;

    private ColorComponentComparator(int component) {
      this.component = component;
    }

    @Override public int compare(Color a, Color b) {
      return (int) Math.signum(a.getComponent(component) - b.getComponent(component));
    }
  }
}
