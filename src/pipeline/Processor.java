package pipeline;

import model.Config;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Processor {
    private static final double MAX_RGB = 255;
    private static final double SIM_THRESHOLD = 20;

    public static Color[][] processImage() {
        int vertStep = Config.bufferedImage.getHeight() / Config.pixelSize;
        int horStep = Config.bufferedImage.getWidth() / Config.pixelSize;
        Color[][] pixelImg = new Color[vertStep][horStep];

        for (int pixelRow = 0; pixelRow < vertStep; pixelRow++) {
            for (int pixelCol = 0; pixelCol < horStep; pixelCol++) {
                pixelImg[pixelRow][pixelCol] = getColorSumAvg(pixelRow, pixelCol);
            }
        }

        return pixelImg;
    }

    private static Color getColorSumAvg(int rowStep, int colStep) {
        double r = 0, g = 0, b = 0;
        int totalPixels = (int) Math.pow(Config.pixelSize, 2);

        int rowLim = Config.pixelSize * rowStep + Config.pixelSize;
        int colLim = Config.pixelSize * colStep + Config.pixelSize;

        for (int row = Config.pixelSize * rowStep; row < rowLim; row++) {
            for (int col = Config.pixelSize * colStep; col < colLim; col++) {
                Color color = new Color(Config.bufferedImage.getRGB(col, row));

                //Normalize rgb values
                r += color.getRed() / MAX_RGB;
                g += color.getGreen() / MAX_RGB;
                b += color.getBlue() / MAX_RGB;
            }
        }

        //Average rgb values
        r = r / totalPixels * MAX_RGB;
        g = g / totalPixels * MAX_RGB;
        b = b / totalPixels * MAX_RGB;

        return new Color((int) r, (int) g, (int) b);
    }

    private static Color getCommonColor(int rowStep, int colStep) {
        HashMap<Integer, Integer> count = new HashMap<>();

        int rowLim = Config.pixelSize * rowStep + Config.pixelSize;
        int colLim = Config.pixelSize * colStep + Config.pixelSize;

        for (int row = Config.pixelSize * rowStep; row < rowLim; row++) {
            for (int col = Config.pixelSize * colStep; col < colLim; col++) {
                Color color = new Color(Config.bufferedImage.getRGB(col, row));
                count.putIfAbsent(color.getRGB(), 0);
                count.put(color.getRGB(), count.get(color.getRGB()) + 1);
            }
        }

        int max = 0;
        int color = 0;

        for(Map.Entry<Integer, Integer> e : count.entrySet()){
            if(max < e.getValue()){
                max = e.getValue();
                color = e.getKey();
            }
        }

        return new Color(color);
    }

    //Deprecated due to complexity... might've been a cool idea if there was more time though :)
    //TODO the idea is to match the highest similar intensity (darkest or lightest) of the pixel's neighbours for edge enhancement
    //TODO perhaps combine with canny edge detection on the original image
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
