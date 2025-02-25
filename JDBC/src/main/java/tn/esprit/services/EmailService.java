package tn.esprit.services;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailService {

    // Method to send email
    public static void sendEmail(String recipient, String subject, String messageText) {
        final String senderEmail = "dalisghaier78910@gmail.com";  // Replace with your Gmail
        final String senderPassword = "zklgldncevudlhyx";  // Replace with the app password you generated

        // Gmail SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Create session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail, "HopitalConnect", "UTF-8"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(messageText);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully to " + recipient);

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            System.err.println("Encoding error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

