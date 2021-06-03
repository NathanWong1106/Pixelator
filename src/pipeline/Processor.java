package pipeline;

import model.Config;

import java.awt.*;
import java.util.Arrays;

public class Processor {
    private static final double MAX_RGB = 255;

    public static Color[][] processImage() {
        int vertStep = Config.bufferedImage.getHeight() / Config.pixelSize;
        int horStep = Config.bufferedImage.getWidth() / Config.pixelSize;
        Color[][] pixelImg = new Color[vertStep][horStep];

        for (int pixelRow = 0; pixelRow < vertStep; pixelRow++) {
            for (int pixelCol = 0; pixelCol < horStep; pixelCol++) {
                pixelImg[pixelRow][pixelCol] = getColourSumAvg(pixelRow, pixelCol);
            }
        }

        return pixelImg;
    }

    private static Color getColourSumAvg(int rowStep, int colStep) {
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
}
