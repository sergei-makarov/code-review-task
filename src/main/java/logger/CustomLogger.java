package main.java.logger;
import java.io.IOException;

public interface CustomLogger {
    void log(LogLevel level, String message) throws IOException;
    void logError(String message);
    void logError(Throwable exception);
    void logWarning(String message);
    boolean logDebug(String message);
}
