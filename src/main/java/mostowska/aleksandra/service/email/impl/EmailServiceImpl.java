package mostowska.aleksandra.service.email.impl;

import lombok.RequiredArgsConstructor;
import mostowska.aleksandra.service.email.EmailService;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.stereotype.Service;

/**
 * EmailServiceImpl is an implementation of the EmailService interface
 * using Simple Java Mail for sending emails.
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final Mailer mailer;

    /**
     * Sends an email to the specified receiver with the given subject and HTML content.
     *
     * @param receiver   The recipient's email address.
     * @param subject    The subject line of the email.
     * @param htmlContent The HTML content of the email body.
     */
    @Override
    public void send(String receiver, String subject, String htmlContent) {
        var email = EmailBuilder
                .startingBlank()
                .withSubject(subject)
                .withHTMLText(htmlContent)
                .to(receiver)
                .buildEmail();

        mailer
                .sendMail(email)
                .thenAccept(result -> {
                    System.out.println("Email sent successfully to " + receiver + ": " + result);
                })
                .exceptionally(ex -> {
                    System.err.println("Failed to send email: " + ex.getMessage());
                    return null;
                });
    }
}
