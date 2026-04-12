package com.aisales.application;

import com.aisales.domain.model.Conversation;
import com.aisales.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;

    @Transactional
    public Conversation findOrCreateConversation(String channel, String userId) {
        Optional<Conversation> existing = conversationRepository.findByUserIdAndChannel(userId, channel);
        if (existing.isPresent()) {
            return existing.get();
        }

        Conversation conversation = Conversation.builder()
                .userId(userId)
                .channel(channel)
                .currentState("NEW")
                .build();

        return conversationRepository.save(conversation);
    }

    @Transactional(readOnly = true)
    public Optional<Conversation> findById(UUID id) {
        return conversationRepository.findById(id);
    }

    @Transactional
    public void updateState(UUID conversationId, String newState) {
        conversationRepository.findById(conversationId).ifPresent(conversation -> {
            conversation.setCurrentState(newState);
            conversationRepository.save(conversation);
        });
    }
}
