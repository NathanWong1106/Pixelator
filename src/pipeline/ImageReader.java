package pipeline;

import model.Config;
import util.Util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Contains methods for reading image input from a file
 */
public class ImageReader {
    public static void readImage(File file) throws IOException {
        //Read in the file as a BufferedImage
        BufferedImage newImg = ImageIO.read(file);

        if (newImg == null) {
            throw new IOException("Invalid File Type");
        }

        //Update the central config
        Config.imgFile = file;
        Config.bufferedImage = newImg;

        int width = Config.bufferedImage.getWidth();
        int height = Config.bufferedImage.getHeight();
        Config.imgArr = new Color[height][width];

        //Read in the image into a color matrix in config
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Config.imgArr[row][col] = new Color(Config.bufferedImage.getRGB(col, row));
            }
        }

        //Update the pixel size options in config
        Config.pixelSizeOptions = Util.getPixelOptions(Config.bufferedImage);
    }
}
