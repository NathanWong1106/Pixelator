package util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public final class Util {
    private static final double SIM_THRESHOLD = 20;

    public static int euclidGCD(int x, int y) {

        int remainder = -1;

        while (remainder != 0) {
            remainder = x < y ? y % x : x % y;
            if (x < y) {
                y = remainder;
            } else {
                x = remainder;
            }
        }

        return x > y ? x : y;
    }

    public static int[] getPixelOptions(BufferedImage img) {
        int gcd = euclidGCD(img.getHeight(), img.getWidth());
        ArrayList<Integer> common = new ArrayList<>();

        for (int i = 1; i <= Math.sqrt(gcd); i++) {
            if (gcd % i == 0) {
                if (gcd / i != i) {
                    common.add(gcd / i);
                }
                common.add(i);
            }
        }

        int[] commonArr = new int[common.size()];

        for (int i = 0; i < commonArr.length; i++) {
            commonArr[i] = common.get(i);
        }

        Arrays.sort(commonArr);
        reversePrimitiveIntArray(commonArr);

        return commonArr;
    }

    public static void reversePrimitiveIntArray(int[] arr) {
        for (int i = 0, mid = arr.length / 2; i < mid; i++) {
            int temp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = temp;
        }
    }

    public static double getEuclideanSimilarity(Color c1, Color c2) {
        //If we treat colours as 3d points in space (r,g,b = x,y,z) we can determine the distance between them and use that measurement for "similarity"
        //https://en.wikipedia.org/wiki/Color_difference#Euclidean
        //dist^2 = (dR)^2 + (dG)^2 + (dB)^2
        int red1 = c1.getRed(), red2 = c2.getRed();
        int green1 = c1.getGreen(), green2 = c2.getGreen();
        int blue1 = c1.getBlue(), blue2 = c2.getBlue();

        double dist = Math.sqrt(Math.pow(red2 - red1, 2) + Math.pow(green2 - green1, 2) + Math.pow(blue2 - blue1, 2));

        return dist;
    }

    public static boolean isColorSimilar(Color c1, Color c2) {
        return getEuclideanSimilarity(c1, c2) < SIM_THRESHOLD;
    }
}
