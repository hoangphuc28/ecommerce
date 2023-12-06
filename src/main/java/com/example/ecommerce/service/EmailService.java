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

        // Load the email template
        String emailTemplate = getEmailTemplate("src/main/resources/templates/mail/verify.html");

        // Replace placeholders in the template with the actual content
        String processedTemplate = emailTemplate.replace("[[content]]", text);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(processedTemplate, true);

        mailSender.send(message);
    }
    public void sendOrderDetail(String to, String subject, Order order) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        // Load the email template
        String emailTemplate = getEmailTemplate("src/main/resources/templates/mail/order.html");

        // Replace placeholders in the template with the actual content
        String processedTemplate = emailTemplate.replace( "[[content]]",  order.getTotalAmount().toString());

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(processedTemplate, true);

        mailSender.send(message);
    }
    public void sendCancelOrder(String to, String subject, Order order) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        // Load the email template
        String emailTemplate = getEmailTemplate("src/main/resources/templates/mail/cancelOrder.html");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String processedTemplate = emailTemplate
                .replace( "[[Customer Name]]",  order.getCustomer().getName())
                .replace( "[[Order Number]]",  order.getId().toString())
                .replace( "[[Order Date]]",  dateFormat.format(order.getOrderDate()))
                .replace( "[[Total Amount]]",  order.getTotalAmount().toString())
                .replace( "[[Cancellation Reason]]",  order.getCancellationReason());

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(processedTemplate, true);

        mailSender.send(message);
    }
    public String getEmailTemplate(String src) throws IOException {
        Path path = Paths.get(src);
        return Files.readString(path);
    }
}
