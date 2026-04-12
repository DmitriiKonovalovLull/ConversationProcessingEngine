package com.aisales.pipeline;

import com.aisales.pipeline.steps.AIProcessingStep;
import com.aisales.pipeline.steps.ConversationStep;
import com.aisales.pipeline.steps.DecisionStep;
import com.aisales.pipeline.steps.ResponseStep;
import com.aisales.pipeline.steps.SaveMessageStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessagePipeline {

    private final ConversationStep conversationStep;
    private final SaveMessageStep saveMessageStep;
    private final AIProcessingStep aiProcessingStep;
    private final DecisionStep decisionStep;
    private final ResponseStep responseStep;

    @Async
    public void process(PipelineContext context) {
        log.info("Starting pipeline processing for user: {}", context.getUserId());
        conversationStep.execute(context);
        saveMessageStep.execute(context);
        aiProcessingStep.execute(context);
        decisionStep.execute(context);
        responseStep.execute(context);
        log.info("Pipeline processing completed for user: {}", context.getUserId());
    }
}
