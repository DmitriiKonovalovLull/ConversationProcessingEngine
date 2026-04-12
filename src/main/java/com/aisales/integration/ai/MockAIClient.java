package com.aisales.integration.ai;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class MockAIClient implements AIClient {

    @Override
    public String analyzeMessage(String text) {
        return "INTERESTED";
    }
}
