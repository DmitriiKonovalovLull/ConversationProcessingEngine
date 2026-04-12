package com.aisales.integration.channel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("prod")
public class WhatsappSender implements MessageSender {

    @Override
    public void send(String channel, String userId, String text) {
        if (!"whatsapp".equalsIgnoreCase(channel)) {
            return;
        }
        log.info("[WHATSAPP] Sending to user {}: {}", userId, text);
    }
}
