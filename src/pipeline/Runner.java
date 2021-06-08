package pipeline;

import model.Command;

import java.io.IOException;

public class Runner implements Command {
    public void run() {
        try {
            ImageWriter.writeImageFromArray(Processor.processImage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
