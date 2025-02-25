package tn.esprit.services;
import java.lang.*;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailService {
    // Method to send email
    public static void sendEmail(String recipient, String subject) {
        System.out.println("Current directory: " + System.getProperty("user.dir"));
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
          // Create the email content (HTML format)
            MimeMultipart multipart = new MimeMultipart("related");

            // HTML part (contains the message and embedded logo)
            MimeBodyPart htmlPart = new MimeBodyPart();
            String htmlContent = "<html><body>"
                    + "<h2>Thank You for Your Reclamation!</h2>"
                    + "<p>Dear User,</p>"
                    + "<p>Your reclamation has been successfully submitted. We will review it and get back to you soon.</p>"
                    + "<p>Thank you!</p>"
                    + "<img src=\"cid:logo\" alt=\"Logo\"/>"  // Embedding logo in HTML content
                    + "</body></html>";

            htmlPart.setContent(htmlContent, "text/html");

            // Attach the HTML part to the multipart
            multipart.addBodyPart(htmlPart);

            // Image part (logo)
            MimeBodyPart imagePart = new MimeBodyPart();
            DataSource fds = new FileDataSource("src/main/resources/Images/logo.jpeg");  // Path to your logo file
            imagePart.setDataHandler(new DataHandler(fds));
            imagePart.setHeader("Content-ID", "<logo>");

            // Attach the image part to the multipart
            multipart.addBodyPart(imagePart);

            // Set the content of the message to the multipart (HTML + image)
            message.setContent(multipart);

            // Send email
            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            System.err.println("Encoding error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

