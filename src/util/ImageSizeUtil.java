package util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageSizeUtil {
    public static final int MAX_IM_SIZE_WIDTH = 700;
    public static final int MAX_IM_SIZE_HEIGHT = 800;

    public static Image getDisplayViableImage(BufferedImage img) {
        if(img.getWidth() < img.getHeight()){
            return img.getScaledInstance(-1, Math.min(img.getHeight(), MAX_IM_SIZE_HEIGHT), BufferedImage.SCALE_SMOOTH);
        } else {
            return img.getScaledInstance(Math.min(img.getWidth(), MAX_IM_SIZE_WIDTH), -1, BufferedImage.SCALE_SMOOTH);
        }
    }
}
