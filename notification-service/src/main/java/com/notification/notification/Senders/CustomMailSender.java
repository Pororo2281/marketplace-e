package com.notification.notification.Senders;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class CustomMailSender implements Sender {

    private JavaMailSender mailSender;
    public CustomMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(String to, String subject, String message) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(message);
        mailSender.send(msg);
    }

}
