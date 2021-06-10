package pipeline;

import model.Command;

import java.io.IOException;

/**
 * This class can be instantiated to be passed into a thread and run
 */
public class Runner implements Command {

    /**
     * Implementation of run() method from the Command interface.
     * Calls the image processing and writing parts of the image pipeline
     */
    public void run() {
        try {
            ImageWriter.writeImageFromArray(Processor.processImage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
