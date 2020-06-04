package main.java.logger;

import main.java.commons.CustomEmailSender;
import main.java.commons.DefaultCustomEmailSender;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.LinkedList;

public class DefaultCustomLogger implements CustomLogger {


    /**
     * A email sender service
     */
    private CustomEmailSender customEmailSender;

    /**
     * List of the admin's emails
     */
    private LinkedList<String> adminEmails;

    public DefaultCustomLogger() {
        this.customEmailSender = new DefaultCustomEmailSender();
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
        return Calendar.getInstance().getTime().toString()
                + "_log.log";
    }

    /**
     * Gets an error message
     *
     * @param level   Log level
     * @param message message's text
     * @return error's message text
     */
    private String getMessage(LogLevel level, String message) {
        return Calendar.getInstance().getTime().toString() +
                level.getShortName() +
                message;
    }

    /**
     * Appends a text line into file
     *
     * @param filename filename
     * @param message  message
     * @throws IOException
     */
    private void appendToFile(String filename, String message) throws IOException {
        FileWriter fileWriter = new FileWriter(filename, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        PrintWriter print = new PrintWriter(bufferedWriter);
        print.println(message);
        print.close();
    }

    /**
     * Logs a message with any level into a log file.
     * Also sends an email to all admins if the error level is error
     *
     * @param level   message level
     * @param message message
     * @throws IOException possible exception
     */
    @Override
    public void log(LogLevel level, String message) throws IOException {
        String errorMessage = getMessage(level, message);
        appendToFile(getLogName(), errorMessage);
        if (level == LogLevel.Error) {
            adminEmails.forEach(email ->
                    customEmailSender.sendEmail(email, "Application error", errorMessage));
        }
    }

    /**
     * Logs a message with an error level into a file and sends an email all admins
     */
    @Override
    public void logError(String message) {
        try {
            log(LogLevel.Error, message);
        } catch (IOException ignored) {
        }
    }

    /**
     * Logs an exception with an error level into a file and sends an email all admins.
     */
    @Override
    public void logError(Throwable exception) {
        logError(exception.getMessage());
    }

    /**
     * Logs an exception with a warning level into file
     */
    @Override
    public void logWarning(String message) {
        try {
            log(LogLevel.Warning, message);
        } catch (IOException ignored) {
        }
    }

    /**
     * Logs a message with a debug level into a file
     * Returns true if a recording was successful or false if it fails
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
}
