package model;

import util.Util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Config {
    public static int pixelSize;
    public static int[] pixelSizeOptions;
    public static String outputFolder;
    public static String outputFileName;
    public static File imgFile;
    public static BufferedImage bufferedImage;
    public static Color[][] imgArr;

    public static void setImage(File file) throws IOException {
        BufferedImage newImg = ImageIO.read(file);

        if (newImg == null) {
            throw new IOException("Invalid File Type");
        }

        imgFile = file;
        bufferedImage = newImg;

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        imgArr = new Color[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                imgArr[row][col] = new Color(bufferedImage.getRGB(col, row));
            }
        }

        pixelSizeOptions = Util.getPixelOptions(bufferedImage);
    }

    public static void setPixelSize(int index) {
        pixelSize = pixelSizeOptions[index];
    }
}
