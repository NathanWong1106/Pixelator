package pipeline;

import model.Config;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageWriter {
    public static void writeImageFromArray(Color[][] imgArr) throws IOException {

        BufferedImage img = new BufferedImage(Config.bufferedImage.getWidth(), Config.bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int row = 0; row < imgArr.length; row++) {
            for (int col = 0; col < imgArr[0].length; col++) {
                fillPixels(row, col, img, imgArr[row][col]);
            }
        }

        File out = new File(Config.outputFolder + "/" + Config.outputFileName + ".png");
        ImageIO.write(img, "png", out);

    }

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
