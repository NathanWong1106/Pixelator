package pipeline;

import util.Command;

import java.io.IOException;

public class Runner implements Command {
    public void run() {
        try {
            System.out.println("runner run");
            ImageWriter.writeImageFromArray(Processor.processImage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
