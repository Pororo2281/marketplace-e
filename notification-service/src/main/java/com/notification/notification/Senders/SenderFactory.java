package com.notification.notification.Senders;

import com.notification.notification.Enum.NotificationEventType;
import org.springframework.stereotype.Component;

@Component
public class SenderFactory {

    private final WelcomeHtmlSender welcomeHtmlSender;
    private final LoginHtmlSender loginHtmlSender;
    private final PasswordResetHtmlSender passwordResetHtmlSender;
    private final PasswordChangedHtmlSender passwordChangedHtmlSender;
    private final OrderCreatedHtmlSender orderCreatedHtmlSender;
    private final OrderPaidHtmlSender orderPaidHtmlSender;

    public SenderFactory(WelcomeHtmlSender welcomeHtmlSender, LoginHtmlSender loginHtmlSender, PasswordResetHtmlSender passwordResetHtmlSender, PasswordChangedHtmlSender passwordChangedHtmlSender, OrderCreatedHtmlSender orderCreatedHtmlSender, OrderPaidHtmlSender orderPaidHtmlSender) {
        this.welcomeHtmlSender = welcomeHtmlSender;
        this.loginHtmlSender = loginHtmlSender;
        this.passwordResetHtmlSender = passwordResetHtmlSender;
        this.passwordChangedHtmlSender = passwordChangedHtmlSender;
        this.orderCreatedHtmlSender = orderCreatedHtmlSender;
        this.orderPaidHtmlSender = orderPaidHtmlSender;
    }

    public HtmlSender getSender(NotificationEventType type){
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
            case ORDER_CREATED -> {
                return orderCreatedHtmlSender;
            }
            case ORDER_PAID -> {
                return orderPaidHtmlSender;
            }
            default -> {
                throw new IllegalArgumentException("Unsupported type: " + type);
            }
        }
    }
}
