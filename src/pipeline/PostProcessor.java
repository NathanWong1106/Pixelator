package pipeline;

import java.awt.*;

public class PostProcessor {
    public static double centralPixelWeight = 10; //basically alpha

    public static Color[][] getWeightedBlur(Color[][] img){
        int height = img.length;
        int width = img[0].length;
        Color[][] post = new Color[img.length][img[0].length];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                double r = 0;
                double g = 0;
                double b = 0;
                double pixels = 1;

                for(int transRow = -1; transRow <= 1; transRow++){
                    int currentRow = row + transRow;

                    if(transRow == 0 || currentRow < 0 || currentRow >= height){
                        continue;
                    }

                    for(int transCol = -1; transCol <= 1; transCol++){
                        int currentCol = col + transCol;

                        if(transCol == 0 || currentCol < 0 || currentCol >= width){
                            continue;
                        }

                        r += img[currentRow][currentCol].getRed() / 255.0;
                        g += img[currentRow][currentCol].getGreen() / 255.0;
                        b += img[currentRow][currentCol].getBlue() / 255.0;
                        pixels++;

                    }
                }

                r += img[row][col].getRed() / 255.0 * centralPixelWeight;
                g += img[row][col].getGreen() / 255.0 * centralPixelWeight;
                b += img[row][col].getBlue() / 255.0 * centralPixelWeight;

                r = r / (pixels + centralPixelWeight) * 255.0;
                g = g / (pixels + centralPixelWeight) * 255.0;
                b = b / (pixels + centralPixelWeight) * 255.0;
                post[row][col] = new Color((int)r, (int)g, (int)b);
            }
        }
        return post;
    }
}
