package model;

/**
 * All processing types
 */
public enum ProcessOption {
    AVERAGE_COLOR_SUM("Averages the colour in each block using the pixels contained in the original image"),
    DOMINANT_COLOR("The colour of the block is determined by which colour is the most popular in the original image"),
    DOMINANT_COLOR_SIMILARITY("The colour of the block is determined by the colour with the most similar colours to it in the original image");

    String description;
    ProcessOption(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
