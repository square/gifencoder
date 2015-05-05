# gifencoder

gifencoder is a pure Java library implementing the [GIF89a specification](http://www.w3.org/Graphics/GIF/spec-gif89a.txt). It does not use AWT's `BufferedImage`, so it can be used on Android and other platforms lacking AWT.

There is currently no support for transparency.


### Quantization and dithering

GIF images are limited to 256 colors. If you supply an image with more colors, gifencoder's default behavior is to perform [median-cut quantization](http://en.wikipedia.org/wiki/Median_cut) with [Floyd-Steinberg dithering](http://en.wikipedia.org/wiki/Floyd%E2%80%93Steinberg_dithering). You can configure a different quantizer or ditherer through `ImageOptions`.


### Basic usage

```java
int[][] rgbDataFrame1 = ...;
int[][] rgbDataFrame2 = ...;
int[][] rgbDataFrame3 = ...;

OutputStream outputStream = new FileOutputStream("test.gif");
ImageOptions options = new ImageOptions();
new GifEncoder(outputStream, width, height, 0)
        .addImage(rgbDataFrame1, options)
        .addImage(rgbDataFrame2, options)
        .addImage(rgbDataFrame3, options)
        .finishEncoding();
outputStream.close();
```


### Gallery

Simple animation with median-cut quantization and Floyd-Steinberg dithering:

![simple animation](gallery/simple-animation.gif)

Lenna, original PNG:

![Lenna original](gallery/lenna-original.png)

Lenna with uniform quantization and Floyd-Steinberg dithering:

![Lenna with uniform quantization and Floyd-Steinberg dithering](gallery/lenna-uniform.gif)

Lenna GIF with median-cut quantization and Floyd-Steinberg dithering:

![Lenna with median-cut quantization and Floyd-Steinberg dithering](gallery/lenna-median-cut.gif)

Lenna GIF with k-means quantization and Floyd-Steinberg dithering:

![Lenna with k-means quantization and Floyd-Steinberg dithering](gallery/lenna-k-means.gif)
