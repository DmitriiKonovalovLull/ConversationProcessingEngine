package com.aisales.integration.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Component
@Profile("prod")
@RequiredArgsConstructor
public class OpenAIClient implements AIClient {

    @Qualifier("openAIWebClient")
    private final WebClient webClient;

    @Override
    public String analyzeMessage(String text) {
        try {
            return webClient.post()
                    .uri("/v1/chat/completions")
                    .bodyValue(buildRequest(text))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("Error calling OpenAI API: {}", e.getMessage());
            return "REQUEST_INFO";
        }
    }

    private Map<String, Object> buildRequest(String text) {
        return Map.of(
                "model", "gpt-3.5-turbo",
                "messages", new Object[]{
                        Map.of("role", "system", "content", "You are a sales assistant. Analyze the message and respond with one of: INTERESTED, NOT_INTERESTED, REQUEST_INFO, OBJECTION, READY_TO_BUY"),
                        Map.of("role", "user", "content", text)
                },
                "temperature", 0.3,
                "max_tokens", 50
        );
    }
}
