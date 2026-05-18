package com.notification.notification.Senders;

import com.notification.notification.Event.UserEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetHtmlSender implements HtmlSender {

    private final JavaMailSender mailSender;
    private final EmailLoader emailLoader;

    public PasswordResetHtmlSender(JavaMailSender mailSender, EmailLoader emailLoader) {
        this.mailSender = mailSender;
        this.emailLoader = emailLoader;
    }

    @Override
    public void send(String to, UserEvent userEvent) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setTo(to);
            helper.setSubject("Ссылка для смены пароля");

            String html =
                    emailLoader.loadTemplate(
                            "reset-password.html"
                    );

            html = html.replace(
                    "${username}",
                    userEvent.getName()
            );

            String resetLink = "https://frontend.com/reset-password?token="
                    + userEvent.getPasswordToken();

            html = html.replace(
                    "${resetLink}",
                    resetLink
            );

            helper.setText(html,true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
