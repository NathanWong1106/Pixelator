package application;

import view.ApplicationFrame;

/**

 Name: Nathan Wong
 Date: June 13, 2021
 Course: ICS4U1-02
 Teacher: Mr. Fernandes
 Title: Pixelator

 Description:
     This program takes an input image and attempts to convert it into pixel art.
     The user is able to customize the output image by selecting the pixel sizes in addition to choosing 1 of 3 processing
     algorithms.

 Features:
    1) Application GUI                                  - Maintaining aspect ratio of images, placement of buttons, labels, sliders, etc
    2) Saving/Writing of processed images               - Allows user to select folder and name to save processed images into
    3) Detail slider                                    - Allows user to select the block size(detail) that the processed image has
    4) Processing algorithm choices:
        4.1) Average colour value                       - Uses the sum average of the rgb channels in a block to determine block colour
        4.2) Dominant colour value                      - Uses the occurrences of a colour in a block to determine block colour
        4.3) Dominant colour value by similarity        - Uses the occurrences of a colour and the similarity of colours around it to determine block colour

 Major Skills:
    1) Swing/GUI
    2) (Multi)Threading
    3) Image/File IO
    4) Algorithms:
        4.1) In place array reversal
        4.2) 3 processing algorithms
        4.3) Euclid's GCD & Finding all common denominators
    5) Datastructures/Objects:
        5.1) Arrays (1D, 2D)
        5.2) ArrayLists
        5.3) HashMap
        5.4) HashTable
        5.5) BufferedImage
        5.6) Color
    6) Interfaces
    7) Enums
    8) Object Oriented Programming/Design

 Areas of Concern:
    1) Because the algorithms do not account for the alpha channel (transparency), processing of images with transparency may result in unintended results.
    2) The labels of the detail slider may look messy if there are too many common denominators of the width and height in an image

 */
public class Main {
    public static void main(String[] args) {
        new ApplicationFrame();
    }
}
