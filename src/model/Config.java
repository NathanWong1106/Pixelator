package model;

import pipeline.ImageReader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Config {
    public static int pixelSize;
    public static int[] pixelSizeOptions;
    public static String outputFolder;
    public static String outputFileName;
    public static File imgFile;
    public static BufferedImage bufferedImage;
    public static BufferedImage convertedImage;
    public static Color[][] imgArr;

    public static void setPixelSize(int index) {
        pixelSize = pixelSizeOptions[index];
    }
}
