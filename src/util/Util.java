package util;

import java.awt.image.BufferedImage;
import java.util.*;

public final class Util {
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
}
