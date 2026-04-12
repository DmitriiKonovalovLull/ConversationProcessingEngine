package com.aisales.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${openai.api-key:${OPENAI_KEY:}}")
    private String openAiApiKey;

    @Value("${openai.base-url:https://api.openai.com}")
    private String openAiBaseUrl;

    @Bean
    public WebClient openAIWebClient() {
        return WebClient.builder()
                .baseUrl(openAiBaseUrl)
                .defaultHeader("Authorization", "Bearer " + openAiApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
