package com.aisales.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MetricsCollector {

    private final MeterRegistry meterRegistry;

    public void messageReceived() {
        meterRegistry.counter("messages.incoming").increment();
    }

    public void messageProcessed() {
        meterRegistry.counter("messages.processed").increment();
    }

    public void pipelineError() {
        meterRegistry.counter("pipeline.errors").increment();
    }

    public void aiCall() {
        meterRegistry.counter("ai.calls").increment();
    }

    public void aiError() {
        meterRegistry.counter("ai.errors").increment();
    }

    public void rateLimitHit() {
        meterRegistry.counter("ratelimit.hits").increment();
    }
}
