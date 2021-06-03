package pipeline;

import model.Config;
import util.Util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageReader {
    public static void readImage(File file) throws IOException {
        BufferedImage newImg = ImageIO.read(file);

        if (newImg == null) {
            throw new IOException("Invalid File Type");
        }

        Config.imgFile = file;
        Config.bufferedImage = newImg;

        int width = Config.bufferedImage.getWidth();
        int height = Config.bufferedImage.getHeight();
        Config.imgArr = new Color[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Config.imgArr[row][col] = new Color(Config.bufferedImage.getRGB(col, row));
            }
        }

        Config.pixelSizeOptions = Util.getPixelOptions(Config.bufferedImage);
    }
}
