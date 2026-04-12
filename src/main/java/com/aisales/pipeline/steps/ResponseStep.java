package com.aisales.pipeline.steps;

import com.aisales.integration.channel.MessageSender;
import com.aisales.pipeline.PipelineContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResponseStep implements PipelineStep {

    private final List<MessageSender> messageSenders;

    @Override
    public void execute(PipelineContext context) {
        String response = generateResponse(context);
        
        for (MessageSender messageSender : messageSenders) {
            messageSender.send(
                    context.getChannel(),
                    context.getUserId(),
                    response
            );
        }
        
        log.info("Response sent for conversation {}: {}", 
                context.getConversation().getId(), 
                response);
    }

    private String generateResponse(PipelineContext context) {
        return switch (context.getDecision().getAction()) {
            case REPLY -> "Thank you for your interest! Our team will contact you shortly.";
            case REQUEST_INFO -> "Could you please provide more details about your requirements?";
            case TRANSFER_TO_HUMAN -> "Connecting you to a human agent...";
            case CLOSE_CONVERSATION -> "Thank you for contacting us. Have a great day!";
            default -> "We received your message.";
        };
    }
}
