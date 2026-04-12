package com.aisales.pipeline.steps;

import com.aisales.application.ConversationService;
import com.aisales.domain.model.Conversation;
import com.aisales.pipeline.PipelineContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConversationStep implements PipelineStep {

    private final ConversationService conversationService;

    @Override
    public void execute(PipelineContext context) {
        Conversation conversation = conversationService.findOrCreateConversation(
                context.getChannel(),
                context.getUserId()
        );
        context.setConversation(conversation);
    }
}
