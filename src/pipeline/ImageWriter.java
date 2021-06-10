package pipeline;

import model.Config;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Contains methods to write 2D color arrays into an image file
 */
public class ImageWriter {
    private static final String FORMAT = ".png";

    /**
     * Writes an image file with the user-selected name and destination.
     * @param imgArr 2D Color matrix representing a pixelated image
     * @implSpec The image array passed in must only represent each pixel "block" of the image. This method can infer the actual block size using the original image.
     * @throws IOException
     */
    public static void writeImageFromArray(Color[][] imgArr) throws IOException {

        BufferedImage img = new BufferedImage(Config.bufferedImage.getWidth(), Config.bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int row = 0; row < imgArr.length; row++) {
            for (int col = 0; col < imgArr[0].length; col++) {
                fillPixels(row, col, img, imgArr[row][col]);
            }
        }

        Config.convertedImage = img;
        File out = new File(Config.outputFolder + "/" + Config.outputFileName + FORMAT);
        ImageIO.write(img, "png", out);

    }

    /**
     * Fills the dimensions of a block in the image with it's specified color
     * @param rowStep the nth row from the top of the current pixel block
     * @param colStep the nth column from the left of the current pixel block
     * @param img BufferedImage to write into
     * @param color Color that the block should be
     */
    private static void fillPixels(int rowStep, int colStep, BufferedImage img, Color color) {
        int rowLim = Config.pixelSize * rowStep + Config.pixelSize;
        int colLim = Config.pixelSize * colStep + Config.pixelSize;

        for (int row = Config.pixelSize * rowStep; row < rowLim; row++) {
            for (int col = Config.pixelSize * colStep; col < colLim; col++) {
                img.setRGB(col, row, color.getRGB());
            }
        }
    }
}
