package com.aisales.pipeline.steps;

import com.aisales.pipeline.PipelineContext;

public interface PipelineStep {

    void execute(PipelineContext context);
}
