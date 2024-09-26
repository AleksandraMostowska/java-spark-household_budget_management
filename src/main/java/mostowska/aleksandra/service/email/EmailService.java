package mostowska.aleksandra.service.email;

/**
 * EmailService interface defines the methods for sending emails.
 */
public interface EmailService {
    /**
     * Sends an email with the specified receiver, subject, and HTML content.
     *
     * @param receiver   The email address of the recipient.
     * @param subject    The subject line of the email.
     * @param htmlContent The HTML content of the email.
     */
    void send(String receiver, String subject, String htmlContent);
}
