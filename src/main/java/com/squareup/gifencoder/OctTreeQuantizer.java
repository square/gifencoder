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

import java.util.HashSet;
import java.util.Set;

/**
 * Implements qct-tree quantization.
 * <p>
 * The principle of algorithm: http://www.microsoft.com/msj/archive/S3F1.aspx
 * </p>
 */
public final class OctTreeQuantizer implements ColorQuantizer {
    public static final OctTreeQuantizer INSTANCE = new OctTreeQuantizer();

    private static char[] mask = new char[]{0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01};
    private int leafCount;
    private int inIndex;

    private Node[] nodeList = new Node[8];

    private OctTreeQuantizer() {
    }

    @Override
    public Set<Color> quantize(Multiset<Color> originalColors, int maxColorCount) {
        Node node = createNode(0);
        Set<Color> distinctElements = originalColors.getDistinctElements();
        for (Color color : distinctElements) {
            addColor(node, color, 0);
            while (leafCount > maxColorCount) {
                reduceTree();
            }
        }
        HashSet<Color> colors = new HashSet<>();
        getColorPalette(node, colors);

        leafCount = 0;
        inIndex = 0;
        for (int i = 0; i < 8; i++) {
            nodeList[i] = null;
        }

        return colors;
    }

    private boolean addColor(Node node, Color color, int inLevel) {
        int nIndex, shift;

        if (node == null) {
            node = createNode(inLevel);
        }

        int red = (int) (color.getComponent(0) * 255);
        int green = (int) (color.getComponent(1) * 255);
        int blue = (int) (color.getComponent(2) * 255);

        if (node.isLeaf) {
            node.pixelCount++;
            node.redSum += red;
            node.greenSum += green;
            node.blueSum += blue;
        } else {
            shift = 7 - inLevel;
            nIndex = (((red & mask[inLevel]) >> shift) << 2) | (((green & mask[inLevel]) >> shift) << 1) | ((blue & mask[inLevel]) >> shift);
            Node tmpNode = node.child[nIndex];
            if (tmpNode == null) {
                tmpNode = createNode(inLevel + 1);
            }
            node.child[nIndex] = tmpNode;
            if (!addColor(node.child[nIndex], color, inLevel + 1)) {
                return false;
            }
        }
        return true;
    }

    private Node createNode(int level) {
        Node node = new Node();
        node.level = level;
        node.isLeaf = (level == 8);
        if (node.isLeaf) {
            leafCount++;
        } else {
            node.next = nodeList[level];
            nodeList[level] = node;
        }
        return node;
    }

    private void reduceTree() {
        int i;
        int redSum = 0, greenSum = 0, blueSum = 0, count = 0;

        for (i = 7; (i > 0) && (nodeList[i] == null); i--) ;

        Node tmpNode = nodeList[i];
        nodeList[i] = tmpNode.next;

        for (i = 0; i < 8; i++) {
            if (tmpNode.child[i] != null) {
                redSum += tmpNode.child[i].redSum;
                greenSum += tmpNode.child[i].greenSum;
                blueSum += tmpNode.child[i].blueSum;
                count += tmpNode.child[i].pixelCount;
                tmpNode.child[i] = null;
                leafCount--;
            }
        }
        tmpNode.isLeaf = true;
        tmpNode.redSum = redSum;
        tmpNode.greenSum = greenSum;
        tmpNode.blueSum = blueSum;
        tmpNode.pixelCount = count;

        leafCount++;
    }

    private void getColorPalette(Node node, Set<Color> colors) {
        if (node.isLeaf) {
            node.colorIndex = inIndex;
            node.redSum = node.redSum / node.pixelCount;
            node.greenSum = node.greenSum / node.pixelCount;
            node.blueSum = node.blueSum / node.pixelCount;
            node.pixelCount = 1;
            inIndex++;
            colors.add(Color.fromRgbInt(node.redSum, node.greenSum, node.blueSum));
        } else {
            for (int i = 0; i < 8; i++) {
                if (node.child[i] != null) {
                    getColorPalette(node.child[i], colors);
                }
            }
        }
    }

    private static final class Node {
        boolean isLeaf;
        int level;
        int colorIndex;

        int redSum;
        int greenSum;
        int blueSum;
        int pixelCount;

        Node[] child = new Node[8];
        Node next;
    }
}
