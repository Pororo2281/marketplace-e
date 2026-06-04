package com.notification.notification.Service;

import com.notification.notification.Event.NotificationEvent;
import com.notification.notification.Senders.HtmlSender;
import com.notification.notification.Senders.SenderFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final SenderFactory senderFactory;

    public EmailService(SenderFactory senderFactory) {
        this.senderFactory = senderFactory;
    }

    public void sendMessage(String to , NotificationEvent notificationEvent ){
        HtmlSender htmlSender = senderFactory.getSender(notificationEvent.getType());
        htmlSender.send(to,notificationEvent);
    }

}
