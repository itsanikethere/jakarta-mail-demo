import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.io.IOException;

import java.util.Properties;
import java.util.Scanner;

public class MailWithFileAttachment {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter your email: ");
        String email = scanner.nextLine();

        System.out.print("Please enter app specific password: ");
        String password = scanner.nextLine();

        System.out.print("Please enter recipient email: ");
        String recipient = scanner.nextLine();

        System.out.print("Please enter message subject: ");
        String subject = scanner.nextLine();

        System.out.print("Please enter message body: ");
        String body = scanner.nextLine();

        System.out.print("Please provide the attachment path: ");
        String path = scanner.nextLine();

        File attachment = new File(path);

        if (!attachment.exists())
            throw new RuntimeException("Attachment path is invalid.");

        if (!attachment.isFile())
            throw new RuntimeException("Attachment invalid.");

        sendEmail(email, password, recipient, subject, body, attachment);
    }

    private static void sendEmail(String email, String password, String recipient, String subject, String body, File file) {
        Properties properties = new Properties();

        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "465");

        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.ssl.enable", "true");

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(email));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            message.setSubject(subject);

            MimeMultipart content = new MimeMultipart();
            MimeBodyPart bodyMine = new MimeBodyPart();
            MimeBodyPart attachmentMine = new MimeBodyPart();

            bodyMine.setText(body);
            try {
                attachmentMine.attachFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            content.addBodyPart(bodyMine);
            content.addBodyPart(attachmentMine);
            message.setContent(content);

            Transport.send(message);
            System.out.println("Email sent successfully.");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
