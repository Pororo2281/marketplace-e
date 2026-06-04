package com.notification.notification.Senders;

import com.notification.notification.Event.UserEvent;

public interface HtmlSender<T> {
    void send(String to, T data);
}
