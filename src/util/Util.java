package util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Utility class containing useful methods that can be used throughout the application
 */
public final class Util {
    private static final double SIM_THRESHOLD = 20;

    /**
     * Returns the greatest common denominator of a pair of numbers
     * @param x num 1
     * @param y num 2
     * @return GCD of x and y
     */
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

    /**
     * An optimized method that returns all common denominators between the height and width of a BufferedImage.
     * The GCD can be used to determine any square pixel sizes that will fit the dimensions of the image perfectly
     * @param img BufferedImage
     * @return int array of Square pixel sizes that fit in the buffered image perfectly
     */
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

    /**
     * Reverses a primitive int array in place
     * @param arr array to reverse
     */
    public static void reversePrimitiveIntArray(int[] arr) {
        for (int i = 0, mid = arr.length / 2; i < mid; i++) {
            int temp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = temp;
        }
    }

    /**
     * If we treat colours as 3d points in space (r,g,b = x,y,z) we can determine the distance between them and use that measurement for "similarity"
     * @param c1 colour 1
     * @param c2 colour 2
     * @return distance between two colours in 3D space
     */
    public static double getEuclideanSimilarity(Color c1, Color c2) {
        //Get coordinates from each colour channel
        int red1 = c1.getRed(), red2 = c2.getRed();
        int green1 = c1.getGreen(), green2 = c2.getGreen();
        int blue1 = c1.getBlue(), blue2 = c2.getBlue();

        //Basically pythagorean theorem but for 3D points
        //dist^2 = (dR)^2 + (dG)^2 + (dB)^2
        double dist = Math.sqrt(Math.pow(red2 - red1, 2) + Math.pow(green2 - green1, 2) + Math.pow(blue2 - blue1, 2));

        return dist;
    }

    /**
     * Using a preset threshold, determines whether a colour is similar to another based on their 3D distance
     * @param c1 colour 1
     * @param c2 colour 2
     * @return True if colours are similar, else false
     */
    public static boolean isColorSimilar(Color c1, Color c2) {
        return getEuclideanSimilarity(c1, c2) < SIM_THRESHOLD;
    }

    /**
     * Returns a string wrapped in html tags that can be used for formatted text
     * @param s string to wrap
     * @return html wrapped string
     */
    public static String getHTMLWrappedString(String s) {
        return "<html>" + s + "</html>";
    }
}
