package util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageSizeUtil {
    public static final int MAX_IM_SIZE_WIDTH = 888;
    public static final int MAX_IM_SIZE_HEIGHT = 950;

    public static Image getDisplayViableImage(BufferedImage img) {
        int width = img.getWidth() > MAX_IM_SIZE_WIDTH ? MAX_IM_SIZE_WIDTH : img.getWidth();
        int height = img.getHeight() > MAX_IM_SIZE_HEIGHT ? MAX_IM_SIZE_HEIGHT : img.getHeight();

        return img.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
    }
}
