package com.aisales.pipeline;

import com.aisales.domain.model.AIDecision;
import com.aisales.domain.model.Conversation;
import com.aisales.domain.model.Message;
import lombok.Data;

@Data
public class PipelineContext {

    private String channel;
    private String userId;
    private String messageText;
    private Conversation conversation;
    private Message message;
    private AIDecision decision;
    private String aiAnalysisResult;
}
