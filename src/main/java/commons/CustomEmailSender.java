package main.java.commons;

public interface CustomEmailSender {
    /**
     * Sends an email
     * @param email destination email address
     * @param title email title
     * @param text email text
     */
    void sendEmail(String email, String title, String text);
}
