package com.banking.bankingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    //send simple text email
    @Async //send in background - dont block main thread
    public void sendSimpleEmail(
            String toEmail,
            String subject,
            String body) {
        try{
            SimpleMailMessage message =
                    new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("Email sent to: " + toEmail);
        } catch(Exception e){
            System.out.println("Email failed: " + e.getMessage());
        }
    }

    //send HTML email - looks professional
    @Async
    public void sendHtmlEmail(
            String toEmail,
            String subject,
            String htmlBody) {
        try{
            MimeMessage message =
                    mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);
            System.out.println("HTML Email sent to: " + toEmail);
        } catch (Exception e){
            System.out.println("Email failed: " + e.getMessage());
        }
    }

    //Welcome email after registration
    public void sendWelcomeEmail(
            String toEmail,
            String name) {
        String subject = "Welcome to SmartBank!";
        String body = "Dear " + name + ",\n\n" +
                "Welcome to SmartBank!\n\n" +
                "Your account has been created successfully.\n" +
                "You can now login and start banking.\n\n" +
                "Thank you for choosing SmatBank!\n\n" +
                "Best regards,\n" +
                "SmartBank Team";
        sendSimpleEmail(toEmail, subject, body);
    }

    //Transaction alert email
    public void sendTransactionAlert(
            String toEmail,
            String name,
            String type,
            Double amount,
            Double balance,
            String accountNumber) {
        String subject = type.equals("DEBIT")
                ? " Money Debited - SmartBank Alert"
                : " Money Credited - SmartBank Alert";

        String htmlBody =  "<html><body>" +
                "<h2 style='color:#1a73e8'>SmartBank</h2>" +
                "<p>Dear <b>" + name + "</b>,</p>" +
                "<p>A transaction has occurred on your account:</p>" +
                "<table border='1' cellpadding='8' " +
                "style='border-collapse:collapse'>" +
                "<tr><td><b>Transaction Type</b></td>" +
                "<td style='color:" +
                (type.equals("DEBIT") ? "red" : "green") +
                "'><b>" + type + "</b></td></tr>" +
                "<tr><td><b>Amount</b></td>" +
                "<td>₹" + amount + "</td></tr>" +
                "<tr><td><b>Account Number</b></td>" +
                "<td>" + accountNumber + "</td></tr>" +
                "<tr><td><b>Available Balance</b></td>" +
                "<td>₹" + balance + "</td></tr>" +
                "</table>" +
                "<p>If you did not authorize this " +
                "transaction, please contact us immediately." +
                "</p>" +
                "<p>Thank you,<br><b>SmartBank Team</b></p>" +
                "</body></html>";

        sendHtmlEmail(toEmail, subject, htmlBody);
    }

    //Login alert
    public void sendLoginAlert(
            String toEmail,
            String name) {
        String subject = " New Login Alert - SmartBank";
        String body = "Dear " + name + ",\n\n" +
                "A nw login was detected on your " +
                "SmartBank account.\n\n" +
                "Time: " + java.time.LocalDateTime.now() +
                "\n\n" +
                "If this was not you, please change " +
                "Your password immediately.\n\n" +
                "SmartBank Team";

        sendSimpleEmail(toEmail, subject, body);
    }
}
