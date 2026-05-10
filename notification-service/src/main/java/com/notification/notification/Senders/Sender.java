package com.notification.notification.Senders;

public interface Sender {
    void send(String to , String subject , String message);
}
