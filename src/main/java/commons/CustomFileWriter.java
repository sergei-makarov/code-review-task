package main.java.commons;

import java.io.IOException;

public interface CustomFileWriter {

    /**
     * Appends a line to the file
     * @param fileName absolute path to the file
     * @param text text for adding to the file
     */
    void appendToFile(String fileName, String text) throws IOException;
}
