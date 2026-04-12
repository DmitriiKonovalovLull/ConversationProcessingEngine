package com.aisales.pipeline.steps;

import com.aisales.application.DecisionService;
import com.aisales.domain.enums.AIAction;
import com.aisales.pipeline.PipelineContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DecisionStep implements PipelineStep {

    private final DecisionService decisionService;

    @Override
    public void execute(PipelineContext context) {
        AIAction action = determineAction(context.getAiAnalysisResult());
        
        var decision = decisionService.processDecision(
                context.getMessage().getId(),
                action,
                "{}",
                "AI analysis result: " + context.getAiAnalysisResult()
        );
        
        context.setDecision(decision);
        log.info("Decision made: {} for message {}", action, context.getMessage().getId());
    }

    private AIAction determineAction(String aiResult) {
        if ("INTERESTED".equals(aiResult)) {
            return AIAction.REPLY;
        }
        return AIAction.REQUEST_INFO;
    }
}
