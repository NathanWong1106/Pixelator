package pipeline;

import model.Config;
import util.Util;

import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Processor {
    private static final double MAX_RGB = 255;

    public static Color[][] processImage() {
        int vertStep = Config.bufferedImage.getHeight() / Config.pixelSize;
        int horStep = Config.bufferedImage.getWidth() / Config.pixelSize;
        Color[][] pixelImg = new Color[vertStep][horStep];

        for (int pixelRow = 0; pixelRow < vertStep; pixelRow++) {
            for (int pixelCol = 0; pixelCol < horStep; pixelCol++) {
                pixelImg[pixelRow][pixelCol] = getDominantColorBySimilarity(pixelRow, pixelCol);
            }
        }

        //return PostProcessor.getWeightedBlur(pixelImg);
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

    //TODO also consider dominant similar colors?
    private static Color getDominantColor(int rowStep, int colStep) {
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

    //By far the least efficient algorithm but also the one with the best results
    private static Color getDominantColorBySimilarity(int rowStep, int colStep) {
        HashMap<Integer, Integer> count = new HashMap<>();
        HashMap<Integer, Integer> similarityCount;

        int rowLim = Config.pixelSize * rowStep + Config.pixelSize;
        int colLim = Config.pixelSize * colStep + Config.pixelSize;

        for (int row = Config.pixelSize * rowStep; row < rowLim; row++) {
            for (int col = Config.pixelSize * colStep; col < colLim; col++) {
                Color color = new Color(Config.bufferedImage.getRGB(col, row));
                count.putIfAbsent(color.getRGB(), 0);
                count.put(color.getRGB(), count.get(color.getRGB()) + 1);
            }
        }

        similarityCount = new HashMap<>(count);

        ArrayList<Map.Entry<Integer, Integer>> list = new ArrayList<>(count.entrySet());
        for(int i = 0, size = list.size(); i < size - 1; i++){
            Color color1 = new Color(list.get(i).getKey());

            for(int j = i + 1; j < size; j++){
                Color color2 = new Color(list.get(j).getKey());

                if(Util.isColorSimilar(color1, color2)){
                    similarityCount.put(color1.getRGB(), similarityCount.get(color1.getRGB()) + count.get(color2.getRGB()));
                    similarityCount.put(color2.getRGB(), similarityCount.get(color2.getRGB()) + count.get(color1.getRGB()));
                }

            }
        }

        int max = 0;
        int color = 0;

        for(Map.Entry<Integer, Integer> e : similarityCount.entrySet()){
            if(max < e.getValue()){
                max = e.getValue();
                color = e.getKey();
            }
        }

        return new Color(color);
    }
}
