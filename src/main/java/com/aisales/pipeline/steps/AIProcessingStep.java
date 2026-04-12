package com.aisales.pipeline.steps;

import com.aisales.integration.ai.AIClient;
import com.aisales.pipeline.PipelineContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AIProcessingStep implements PipelineStep {

    private final AIClient aiClient;

    @Override
    public void execute(PipelineContext context) {
        String result = aiClient.analyzeMessage(
                context.getMessage().getText()
        );
        context.setAiAnalysisResult(result);
    }
}
