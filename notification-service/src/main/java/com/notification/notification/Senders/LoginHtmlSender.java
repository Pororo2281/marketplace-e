package com.notification.notification.Senders;

import com.notification.notification.Event.UserEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class LoginHtmlSender implements HtmlSender<UserEvent>{

    private final JavaMailSender mailSender;
    private final EmailLoader emailLoader;

    public LoginHtmlSender(EmailLoader emailLoader, JavaMailSender mailSender) {
        this.emailLoader = emailLoader;
        this.mailSender = mailSender;
    }

    @Override
    public void send(String to, UserEvent userEvent) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setTo(to);
            helper.setSubject("Совершен вход в систему!");

            String html =
                    emailLoader.loadTemplate(
                            "login.html"
                    );

            html = html.replace(
                    "${username}",
                    userEvent.getName()
            );

            html = html.replace(
                    "${actionDate}",
                    DateTimeFormatter.ofPattern("dd.MM.yyyy")
                            .withZone(ZoneId.systemDefault())
                            .format(userEvent.getActionTime())
            );

            html = html.replace(
                    "${actionTime}",
                    DateTimeFormatter.ofPattern("HH:mm")
                            .withZone(ZoneId.systemDefault())
                            .format(userEvent.getActionTime())
            );

            helper.setText(html,true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }




}
