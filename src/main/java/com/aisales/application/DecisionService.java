package com.aisales.application;

import com.aisales.domain.model.AIDecision;
import com.aisales.domain.enums.AIAction;
import com.aisales.domain.model.Message;
import com.aisales.repository.AIDecisionRepository;
import com.aisales.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DecisionService {

    private final AIDecisionRepository aiDecisionRepository;
    private final MessageRepository messageRepository;

    @Transactional
    public AIDecision processDecision(UUID messageId, AIAction action, String parameters, String reasoning) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found: " + messageId));

        AIDecision decision = AIDecision.builder()
                .message(message)
                .action(action)
                .parameters(parameters)
                .reasoning(reasoning)
                .build();

        AIDecision saved = aiDecisionRepository.save(decision);
        log.info("AI decision processed: action={}, messageId={}", action, messageId);
        return saved;
    }

    @Transactional(readOnly = true)
    public Optional<AIDecision> findByMessageId(UUID messageId) {
        return aiDecisionRepository.findByMessageId(messageId);
    }

    @Transactional
    public void markAsExecuted(UUID decisionId) {
        aiDecisionRepository.findById(decisionId).ifPresent(decision -> {
            decision.setExecuted(true);
            aiDecisionRepository.save(decision);
        });
    }
}
