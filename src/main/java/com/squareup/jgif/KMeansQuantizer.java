package com.squareup.jgif;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Uses k-means clustering for color quantization. This may yield better results than
 * {@link MedianCutQuantizer}, but it is considerably slower so it is not recommended for large
 * images.
 */
public final class KMeansQuantizer implements ColorQuantizer {
  public static final KMeansQuantizer INSTANCE = new KMeansQuantizer();
  private static final Random random = new Random();

  private KMeansQuantizer() {}

  @Override public Set<Color> quantize(Set<Color> originalColors, int maxColorCount) {
    // We use the Forgy initialization method -- choose random colors as initial cluster centroids.
    List<Color> colorsAsList = new ArrayList<>(originalColors);
    Set<Color> centroids = new HashSet<>();
    for (int i = 0; i < maxColorCount; ++i) {
      centroids.add(randomListElement(colorsAsList));
    }

    for (;;) {
      Collection<Set<Color>> clusters = assignClusters(originalColors, centroids);
      Set<Color> newCentroids = recomputeClusterCentroids(clusters);
      if (newCentroids.equals(centroids)) {
        // We've converged.
        return centroids;
      }
      centroids = newCentroids;
    }
  }

  private static Collection<Set<Color>> assignClusters(Set<Color> colors, Set<Color> centroids) {
    Map<Color, Set<Color>> clustersByCentroid = new HashMap<>();
    for (Color centroid : centroids) {
      clustersByCentroid.put(centroid, new HashSet<Color>());
    }
    for (Color color : colors) {
      Color closestCentroid = color.getNearestColor(centroids);
      clustersByCentroid.get(closestCentroid).add(color);
    }
    return clustersByCentroid.values();
  }

  private static Set<Color> recomputeClusterCentroids(Collection<Set<Color>> clusters) {
    Set<Color> centroids = new HashSet<>();
    for (Set<Color> cluster : clusters) {
      centroids.add(Color.getCentroid(cluster));
    }
    return centroids;
  }

  /**
   * Returns a random element from a list.
   */
  private static <T> T randomListElement(List<T> list) {
    return list.get(random.nextInt(list.size()));
  }
}
