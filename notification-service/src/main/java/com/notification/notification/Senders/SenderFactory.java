package com.notification.notification.Senders;

import com.notification.notification.Enum.UserEventType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SenderFactory {

    private final WelcomeHtmlSender welcomeHtmlSender;
    private final LoginHtmlSender loginHtmlSender;
    private final PasswordResetHtmlSender passwordResetHtmlSender;
    private final PasswordChangedHtmlSender passwordChangedHtmlSender;

    public SenderFactory(WelcomeHtmlSender welcomeHtmlSender, LoginHtmlSender loginHtmlSender, PasswordResetHtmlSender passwordResetHtmlSender, PasswordChangedHtmlSender passwordChangedHtmlSender) {
        this.welcomeHtmlSender = welcomeHtmlSender;
        this.loginHtmlSender = loginHtmlSender;
        this.passwordResetHtmlSender = passwordResetHtmlSender;
        this.passwordChangedHtmlSender = passwordChangedHtmlSender;
    }

    public HtmlSender getSender(UserEventType type){
        switch (type){
            case LOGIN -> {
                return loginHtmlSender;
            }
            case CREATED -> {
                return welcomeHtmlSender;
            }
            case RESET_PASSWORD_REQUESTED -> {
                return passwordResetHtmlSender;
            }
            case PASSWORD_CHANGED -> {
                return passwordChangedHtmlSender;
            }
            default -> {
                return null;
            }
        }
    }
}
