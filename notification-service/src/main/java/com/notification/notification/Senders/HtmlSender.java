package com.notification.notification.Senders;

import com.notification.notification.Event.UserEvent;

public interface HtmlSender {
    void send(String to, UserEvent userEvent);
}
