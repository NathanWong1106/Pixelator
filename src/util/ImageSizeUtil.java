package util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageSizeUtil {
    public static final int MAX_IM_SIZE_WIDTH = 700;
    public static final int MAX_IM_SIZE_HEIGHT = 800;

    public static Image getDisplayViableImage(BufferedImage img) {
        int width = img.getWidth() > MAX_IM_SIZE_WIDTH ? MAX_IM_SIZE_WIDTH : img.getWidth();
        //int height = img.getHeight() > MAX_IM_SIZE_HEIGHT ? MAX_IM_SIZE_HEIGHT : img.getHeight();

        //Note: -1 will maintain aspect ratio depending on the width
        return img.getScaledInstance(width, -1, BufferedImage.SCALE_SMOOTH);
    }
}
