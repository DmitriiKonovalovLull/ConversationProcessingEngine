package com.aisales.pipeline.steps;

import com.aisales.application.MessageService;
import com.aisales.domain.model.Message;
import com.aisales.pipeline.PipelineContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveMessageStep implements PipelineStep {

    private final MessageService messageService;

    @Override
    public void execute(PipelineContext context) {
        Message message = messageService.saveIncomingMessage(
                context.getConversation(),
                context.getMessageText()
        );
        context.setMessage(message);
    }
}
