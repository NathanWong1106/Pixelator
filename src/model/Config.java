package model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Class that contains global variables accessed by other classes in the pipeline and view.
 * Provides a central/single source of truth for the entire program
 */
public class Config {
    public static int pixelSize;
    public static int[] pixelSizeOptions;
    public static String outputFolder;
    public static String outputFileName;
    public static File imgFile;
    public static BufferedImage bufferedImage;
    public static BufferedImage convertedImage;
    public static Color[][] imgArr;
    public static ProcessOption processOption = ProcessOption.AVERAGE_COLOR_SUM;

    public static void setPixelSize(int index) {
        pixelSize = pixelSizeOptions[index];
    }
}
