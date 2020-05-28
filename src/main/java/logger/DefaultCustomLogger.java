package main.java.logger;

import main.java.commons.CustomEmailSender;
import main.java.commons.CustomFileWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

@Service
public class DefaultCustomLogger implements CustomLogger {

    /**
     * A file writer service
     */
    private CustomFileWriter customFileWriter;

    /**
     * A email sender service
     */
    private CustomEmailSender customEmailSender;

    /**
     * List of the admin's emails
     */
    private LinkedList<String> adminEmails;

    @Autowired
    public DefaultCustomLogger(CustomFileWriter customFileWriter, CustomEmailSender customEmailSender) {
        this.customFileWriter = customFileWriter;
        this.customEmailSender = customEmailSender;

        adminEmails = new LinkedList<>();
        adminEmails.add("admin@miro.com");
        adminEmails.add("duty@miro.com");
        adminEmails.add("devops@miro.com");
    }

    /**
     * Returns a filename of the log file
     *
     * @return log's filename
     */
    private String getLogName() {
        DateFormat df = new SimpleDateFormat("yy_MM_dd");
        Date currentDate = new Date();

        StringBuilder builder = new StringBuilder();
        builder.append(df.format(currentDate));
        builder.append("_log.log");

        return builder.toString();
    }

    /**
     * Gets an error message
     * @param level Log level
     * @param message income message's text
     * @return error's message text
     */
    private String getMessage(LogLevel level, String message) {
        String delimiter = "    ";
        StringBuilder builder = new StringBuilder();

        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date currentDate = new Date();
        builder.append(df.format(currentDate));
        builder.append(delimiter);

        switch (level) {
            case Debug:
                builder.append("DBG");
            case Error:
                builder.append("ERR");
            case Warning:
                builder.append("WRN");
            default:
                builder.append("UNKNOWN");
        }

        builder.append(delimiter);
        builder.append(message);

        return builder.toString();
    }

    /**
     * Logs a message with any level
     * @param level message level
     * @param message message
     * @throws IOException possible exception
     */
    @Override
    public void log(LogLevel level, String message) throws IOException {
        String errorMessage = getMessage(level, message);
        customFileWriter.appendToFile(getLogName(), errorMessage);
        if (level == LogLevel.Error) {
            adminEmails.forEach(email ->
                    customEmailSender.sendEmail(email, "Application error", errorMessage));
        }
    }

    /**
     * Logs a message with an error level
     * @param message message
     */
    @Override
    public void logError(String message) {
        try {
            log(LogLevel.Error, message);
        } catch (IOException ignored) {
        }
    }

    /**
     * Logs a message with a warning level
     * @param message message
     */
    @Override
    public void logWarning(String message) {
        try {
            log(LogLevel.Warning, message);
        } catch (IOException ignored) {
        }
    }

    /**
     * Logs a message with a debug level
     * @param message message
     */
    @Override
    public boolean logDebug(String message) {
        try {
            log(LogLevel.Debug, message);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Log error from exception
     * @param exception exception
     */
    @Override
    public void logError(Throwable exception) {
        logError(exception.getMessage());
    }
}
