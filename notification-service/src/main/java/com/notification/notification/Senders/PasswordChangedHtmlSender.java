package com.notification.notification.Senders;

import com.notification.notification.Event.UserEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class PasswordChangedHtmlSender implements HtmlSender<UserEvent> {

    private final JavaMailSender mailSender;
    private final EmailLoader emailLoader;

    public PasswordChangedHtmlSender(JavaMailSender mailSender, EmailLoader emailLoader) {
        this.mailSender = mailSender;
        this.emailLoader = emailLoader;
    }


    @Override
    public void send(String to, UserEvent userEvent) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setTo(to);
            helper.setSubject("Пароль успешно изменен!");

            String html =
                    emailLoader.loadTemplate(
                            "password-changed.html"
                    );

            html = html.replace(
                    "${username}",
                    userEvent.getName()
            );


            helper.setText(html,true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
