package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String text) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(verifyTemplate(text), true);

        mailSender.send(message);
    }
    public void sendOrderDetail(String to, String subject, Order order) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText("Order Successfully!\nTotal: "+order.getTotalAmount().toString());

        mailSender.send(message);
    }
    public void sendCancelOrder(String to, String subject, Order order) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());




        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(cancelOrderTemplate(order), true);

        mailSender.send(message);
    }

    public String verifyTemplate(String content) {
        return  String.format("<h1>Welcome to our Application</h1>%n" +
                "<p>Click the button to verify account</p>%n" +
                "<a href=\"http://localhost:8080/verify?token=%s\"%n" +
                "   style=\"display: inline-block; padding: 10px 20px; background-color: #007bff; color: #fff; text-decoration: none;\">\n" +
                "    Verify</a>", content);
    }

    public String cancelOrderTemplate(Order order) {
        String emailTemplate = "<div class=\"container mt-4\">\n" +
                "  <div class=\"jumbotron text-center\">\n" +
                "    <h1 class=\"display-4\" style=\"color: #e74c3c;\">Order Cancellation Confirmation</h1>\n" +
                "  </div>\n" +
                "\n" +
                "  <div class=\"card\">\n" +
                "    <div class=\"card-body\">\n" +
                "      <p class=\"card-text\">Dear %s,</p>\n" +
                "      <p class=\"card-text\">We regret to inform you that your order has been canceled. Below are the details:</p>\n" +
                "\n" +
                "      <table class=\"table table-bordered\">\n" +
                "        <tr>\n" +
                "          <th scope=\"row\">Order Number:</th>\n" +
                "          <td>%s</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <th scope=\"row\">Order Date:</th>\n" +
                "          <td>%s</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <th scope=\"row\">Total Amount:</th>\n" +
                "          <td>%s</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <th scope=\"row\">Cancellation Reason:</th>\n" +
                "          <td>%s</td>\n" +
                "        </tr>\n" +
                "      </table>\n" +
                "\n" +
                "      <p class=\"card-text\">We apologize for any inconvenience caused. If you have any questions or concerns, please feel free to contact our customer support.</p>\n" +
                "      <p class=\"card-text\">Thank you for choosing our service.</p>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</div>";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Format the template with actual values
        String formattedEmail = String.format(emailTemplate, order.getCustomer().getName(),
                order.getId().toString(), dateFormat.format(order.getOrderDate()), order.getTotalAmount().toString(), order.getCancellationReason());

        return formattedEmail;
    }
}
