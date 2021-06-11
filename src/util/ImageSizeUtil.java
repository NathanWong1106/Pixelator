package util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Utility to resize images and maintain aspect ratio on each JLabel
 */
public class ImageSizeUtil {
    public static final int MAX_IM_SIZE_WIDTH = 700;
    public static final int MAX_IM_SIZE_HEIGHT = 800;

    /**
     * Returns a scaled image that fits inside the width and height bounds
     * @param img BufferedImage to get scaled instance
     * @return scaled image
     */
    public static Image getDisplayViableImage(BufferedImage img) {
        //Scale based on height if portrait, else scale based on width
        if(img.getWidth() < img.getHeight()){
            return img.getScaledInstance(-1, Math.min(img.getHeight(), MAX_IM_SIZE_HEIGHT), BufferedImage.SCALE_SMOOTH);
        } else {
            return img.getScaledInstance(Math.min(img.getWidth(), MAX_IM_SIZE_WIDTH), -1, BufferedImage.SCALE_SMOOTH);
        }
    }
}
