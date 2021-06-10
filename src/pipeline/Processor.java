package pipeline;

import model.Config;
import util.Util;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The Processor contains all methods that turn a regular image into a pixelated image
 */
public class Processor {
    //The maximum value of an rgb value (used to normalize from [0-1])
    private static final double MAX_RGB = 255;

    /**
     * Generates the correct block size to process the image and processes the image using the user-selected algorithm
     * @return 2D matrix of Colors representing the pixelated image
     * @implNote Returned matrix is of the number of vertical / horizontal blocks (not the size of the original image)
     */
    public static Color[][] processImage() {
        //Calculate the number of rows/columns of pixels in the converted image
        int vertStep = Config.bufferedImage.getHeight() / Config.pixelSize;
        int horStep = Config.bufferedImage.getWidth() / Config.pixelSize;
        Color[][] pixelImg = new Color[vertStep][horStep];

        //Find the color for each block in the converted image using the chosen algorithm
        for (int pixelRow = 0; pixelRow < vertStep; pixelRow++) {
            for (int pixelCol = 0; pixelCol < horStep; pixelCol++) {

                switch(Config.processOption) {
                    case AVERAGE_COLOR_SUM:
                        pixelImg[pixelRow][pixelCol] = getColorSumAvg(pixelRow, pixelCol);
                        break;
                    case DOMINANT_COLOR:
                        pixelImg[pixelRow][pixelCol] = getDominantColor(pixelRow, pixelCol);
                        break;
                    case DOMINANT_COLOR_SIMILARITY:
                        pixelImg[pixelRow][pixelCol] = getDominantColorBySimilarity(pixelRow, pixelCol);
                        break;
                }
            }
        }
        return pixelImg;
    }

    /**
     * Returns the colour of a pixel block by averaging all pixel colours in the block area
     * @param rowStep the nth row from the top of the current pixel block
     * @param colStep the nth column from the left of the current pixel block
     * @return Color of the pixel block
     */
    private static Color getColorSumAvg(int rowStep, int colStep) {
        double r = 0, g = 0, b = 0;
        int totalPixels = (int) Math.pow(Config.pixelSize, 2);

        //Bottom/Right side limits of the current block we are processing
        int rowLim = Config.pixelSize * rowStep + Config.pixelSize;
        int colLim = Config.pixelSize * colStep + Config.pixelSize;

        //Sum the total normalized rgb values of each pixel within the block
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

    /**
     * Returns the colour of a pixel block by getting the most dominant colour in the block area
     * @param rowStep the nth row from the top of the current pixel block
     * @param colStep the nth column from the left of the current pixel block
     * @return Color of the pixel block
     */
    private static Color getDominantColor(int rowStep, int colStep) {
        //Get a count of the occurrences of each color in the block
        HashMap<Integer, Integer> count = getColorOccurrences(rowStep, colStep);

        //Find the most common colour in the block then return it
        return new Color(getKeyWithLargestVal(count));
    }

    /**
     * Returns the colour of a pixel block by taking a count of all colours
     * then returns the Colour with the largest amount of similarly coloured pixels within the block.
     * @param rowStep the nth row from the top of the current pixel block
     * @param colStep the nth column from the left of the current pixel block
     * @implNote By far the least efficient algorithm but results may be improved depending on the image
     * @return Color of the pixel block
     */
    private static Color getDominantColorBySimilarity(int rowStep, int colStep) {
        HashMap<Integer, Integer> count = getColorOccurrences(rowStep, colStep);
        HashMap<Integer, Integer> similarityCount = new HashMap<>(count);
        ArrayList<Map.Entry<Integer, Integer>> list = new ArrayList<>(count.entrySet());

        //Compare and sum the values of similar colors
        for(int i = 0, size = list.size(); i < size - 1; i++){
            Color color1 = new Color(list.get(i).getKey());

            for(int j = i + 1; j < size; j++){
                Color color2 = new Color(list.get(j).getKey());

                //If the color is similar then increment each key's count by the other's respective value
                if(Util.isColorSimilar(color1, color2)){
                    similarityCount.put(color1.getRGB(), similarityCount.get(color1.getRGB()) + count.get(color2.getRGB()));
                    similarityCount.put(color2.getRGB(), similarityCount.get(color2.getRGB()) + count.get(color1.getRGB()));
                }

            }
        }

        //Return the color with the most common colors to it
        return new Color(getKeyWithLargestVal(similarityCount));
    }

    /**
     * For a given block, returns the occurrences of each color contained in it
     * @param rowStep the nth row from the top of the current pixel block
     * @param colStep the nth column from the left of the current pixel block
     * @return HashMap of the Color(int representation) and the number of times it has occurred
     */
    private static HashMap<Integer, Integer> getColorOccurrences(int rowStep, int colStep) {
        HashMap<Integer, Integer> count = new HashMap<>();

        //Bottom/Right side limits of the current block we are processing
        int rowLim = Config.pixelSize * rowStep + Config.pixelSize;
        int colLim = Config.pixelSize * colStep + Config.pixelSize;

        //Find the number of occurrences of each pixel colour in the block
        for (int row = Config.pixelSize * rowStep; row < rowLim; row++) {
            for (int col = Config.pixelSize * colStep; col < colLim; col++) {
                Color color = new Color(Config.bufferedImage.getRGB(col, row));
                count.putIfAbsent(color.getRGB(), 0);
                count.put(color.getRGB(), count.get(color.getRGB()) + 1);
            }
        }

        return count;
    }

    /**
     * Returns the key of a HashMap with the largest integer value
     * @param map {@code HashMap<T, Integer>} containing the values
     * @param <T> Type of keys that the map uses
     * @return key with largest integer value
     */
    private static <T> T getKeyWithLargestVal(HashMap<T, Integer> map) {
        int max = 0;
        T maxKey = null;

        for(Map.Entry<T, Integer> e : map.entrySet()){
            if(max < e.getValue()){
                max = e.getValue();
                maxKey = e.getKey();
            }
        }

        return maxKey;
    }
}
