package com.notification.notification.Senders;

import com.notification.notification.Event.UserEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class WelcomeHtmlSender implements HtmlSender{

    private final JavaMailSender mailSender;
    private final EmailLoader emailLoader;

    public WelcomeHtmlSender(EmailLoader emailLoader, JavaMailSender mailSender) {
        this.emailLoader = emailLoader;
        this.mailSender = mailSender;
    }

    @Override
    public void send(String to,UserEvent userEvent) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setTo(to);
            helper.setSubject("Добро пожаловать!");

            String html =
                    emailLoader.loadTemplate(
                            "welcome.html"
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
