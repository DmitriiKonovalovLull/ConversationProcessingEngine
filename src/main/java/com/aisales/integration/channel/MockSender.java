package com.aisales.integration.channel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("dev")
public class MockSender implements MessageSender {

    @Override
    public void send(String channel, String userId, String text) {
        log.info("[MOCK SENDER] Channel: {}, User: {}, Message: {}", channel, userId, text);
    }
}
