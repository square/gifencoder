package com.squareup.gifencoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Uses k-means clustering for color quantization. This may yield better results than {@link
 * MedianCutQuantizer}, but it is considerably slower so it is not recommended for large images.
 */
public final class KMeansQuantizer implements ColorQuantizer {
  public static final KMeansQuantizer INSTANCE = new KMeansQuantizer();

  private KMeansQuantizer() {
  }

  @Override public Set<Color> quantize(Multiset<Color> originalColors, int maxColorCount) {
    Collection<Color> centroids = getInitialCentroids(originalColors, maxColorCount);

    int round = 0;
    while (true) {
      System.out.println(round++); // TODO
      Collection<Multiset<Color>> clusters = assignClusters(originalColors, centroids);
      Collection<Color> newCentroids = recomputeClusterCentroids(clusters);
      if (newCentroids.equals(centroids)) {
        // We've converged.
        return new HashSet<>(centroids);
      }
      centroids = newCentroids;
    }
  }

  private Collection<Color> getInitialCentroids(Multiset<Color> originalColors, int maxColorCount) {
    // We use the Forgy initialization method: choose random colors as initial cluster centroids.
    List<Color> colorList = new ArrayList<>(originalColors);
    Collections.shuffle(colorList);
    return colorList.subList(0, maxColorCount);
  }

  private static Collection<Multiset<Color>> assignClusters(Multiset<Color> originalColors,
      Collection<Color> centroids) {
    Map<Color, Multiset<Color>> clustersByCentroid = new HashMap<>(centroids.size());
    for (Color centroid : centroids) {
      clustersByCentroid.put(centroid, new HashMultiset<Color>());
    }
    for (Color color : originalColors.getDistinctElements()) {
      int colorCount = originalColors.count(color);
      Color closestCentroid = color.getNearestColor(centroids);
      clustersByCentroid.get(closestCentroid).add(color, colorCount);
    }
    return clustersByCentroid.values();
  }

  private static Collection<Color> recomputeClusterCentroids(Collection<Multiset<Color>> clusters) {
    Collection<Color> centroids = new ArrayList<>(clusters.size());
    for (Multiset<Color> cluster : clusters) {
      centroids.add(Color.getCentroid(cluster));
    }
    return centroids;
  }
}
