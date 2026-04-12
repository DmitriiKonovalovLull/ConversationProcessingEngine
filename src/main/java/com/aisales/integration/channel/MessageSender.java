package com.aisales.integration.channel;

public interface MessageSender {

    void send(String channel, String userId, String text);
}
