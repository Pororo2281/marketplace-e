package com.notification.notification.Service;

import com.notification.notification.Enum.UserEventType;
import com.notification.notification.Event.UserEvent;
import com.notification.notification.Senders.HtmlSender;
import com.notification.notification.Senders.SenderFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final SenderFactory senderFactory;

    public EmailService(SenderFactory senderFactory) {
        this.senderFactory = senderFactory;
    }

    public void sendMessage(String to , UserEvent userEvent){
        HtmlSender htmlSender = senderFactory.getSender(userEvent.getEventType());
        htmlSender.send(to,userEvent);
    }

}
