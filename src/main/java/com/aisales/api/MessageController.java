package com.aisales.api;

import com.aisales.api.dto.IncomingMessageRequest;
import com.aisales.api.dto.SendMessageRequest;
import com.aisales.metrics.MetricsCollector;
import com.aisales.pipeline.MessagePipeline;
import com.aisales.pipeline.PipelineContext;
import com.aisales.security.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessagePipeline messagePipeline;
    private final RateLimiter rateLimiter;
    private final MetricsCollector metricsCollector;

    @PostMapping("/incoming")
    public ResponseEntity<Void> receiveIncomingMessage(@Valid @RequestBody IncomingMessageRequest request) {
        metricsCollector.messageReceived();
        
        if (!rateLimiter.allowed(request.userId())) {
            metricsCollector.rateLimitHit();
            log.warn("Rate limit exceeded for user: {}", request.userId());
            return ResponseEntity.status(429).build();
        }
        
        PipelineContext context = new PipelineContext();
        context.setChannel(request.channel());
        context.setUserId(request.userId());
        context.setMessageText(request.text());

        messagePipeline.process(context);
        metricsCollector.messageProcessed();
        
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@Valid @RequestBody SendMessageRequest request) {
        metricsCollector.messageReceived();

        PipelineContext context = new PipelineContext();
        context.setChannel(request.channel());
        context.setUserId(request.userId());
        context.setMessageText(request.text());

        messagePipeline.process(context);
        metricsCollector.messageProcessed();

        return ResponseEntity.ok().build();
    }
}
