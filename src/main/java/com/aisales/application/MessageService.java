package com.aisales.application;

import com.aisales.domain.model.Conversation;
import com.aisales.domain.model.Message;
import com.aisales.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public Message saveMessage(Conversation conversation, String role, String text, Double confidenceScore) {
        Message message = Message.builder()
                .conversation(conversation)
                .role(role)
                .text(text)
                .confidenceScore(confidenceScore)
                .build();

        return messageRepository.save(message);
    }

    @Transactional
    public Message saveIncomingMessage(Conversation conversation, String text) {
        Message message = Message.builder()
                .conversation(conversation)
                .role("USER")
                .text(text)
                .confidenceScore(null)
                .build();

        return messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<Message> getHistory(Conversation conversation) {
        return messageRepository.findByConversationOrderByCreatedAtAsc(conversation);
    }

    @Transactional(readOnly = true)
    public List<Message> getHistory(UUID conversationId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    @Transactional(readOnly = true)
    public List<Message> getHistory(UUID conversationId, int limit) {
        List<Message> messages = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        int size = messages.size();
        return messages.subList(Math.max(0, size - limit), size);
    }
}
