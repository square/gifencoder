package com.squareup.gifencoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
    Map<Color, Multiset<Color>> clustersByCentroid = new LinkedHashMap<>();
    Set<Color> centroidsToRecompute = getInitialCentroids(originalColors, maxColorCount);
    for (Color centroid : centroidsToRecompute) {
      clustersByCentroid.put(centroid, new HashMultiset<Color>());
    }
    for (Color color : originalColors.getDistinctElements()) {
      int count = originalColors.count(color);
      clustersByCentroid.get(color.getNearestColor(centroidsToRecompute)).add(color, count);
    }

    while (!centroidsToRecompute.isEmpty()) {
      recomputeCentroids(clustersByCentroid, centroidsToRecompute);
      centroidsToRecompute.clear();

      Set<Color> allCentroids = clustersByCentroid.keySet();
      for (Color centroid : clustersByCentroid.keySet()) {
        Multiset<Color> cluster = clustersByCentroid.get(centroid);
        for (Color color : new ArrayList<>(cluster.getDistinctElements())) {
          Color newCentroid = color.getNearestColor(allCentroids);
          if (newCentroid != centroid) {
            int count = cluster.count(color);
            Multiset<Color> newCluster = clustersByCentroid.get(newCentroid);

            cluster.remove(color, count);
            newCluster.add(color, count);

            centroidsToRecompute.add(centroid);
            centroidsToRecompute.add(newCentroid);
          }
        }
      }
    }

    return clustersByCentroid.keySet();
  }

  void recomputeCentroids(Map<Color, Multiset<Color>> clustersByCentroid,
      Set<Color> centroidsToRecompute) {
    for (Color oldCentroid : centroidsToRecompute) {
      Multiset<Color> cluster = clustersByCentroid.get(oldCentroid);
      Color newCentroid = Color.getCentroid(cluster);
      clustersByCentroid.remove(oldCentroid);
      clustersByCentroid.put(newCentroid, cluster);
    }
  }

  private Set<Color> getInitialCentroids(Multiset<Color> originalColors, int maxColorCount) {
    // We use the Forgy initialization method: choose random colors as initial cluster centroids.
    List<Color> colorList = new ArrayList<>(originalColors.getDistinctElements());
    Collections.shuffle(colorList);
    return new HashSet<>(colorList.subList(0, maxColorCount));
  }
}
