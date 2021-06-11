package model;

/**
 * Interface for a command (passing a method like a variable)
 */
public interface Command {
    /**
     * Run method carries the method(s) to be called through this command
     */
    void run();
}
