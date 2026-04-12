package com.aisales.api;

import com.aisales.domain.model.Conversation;
import com.aisales.domain.model.Message;
import com.aisales.repository.ConversationRepository;
import com.aisales.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    @GetMapping
    public List<Conversation> getConversations(@RequestParam String userId) {
        return conversationRepository.findByUserId(userId);
    }

    @GetMapping("/{id}/messages")
    public List<Message> getMessages(@PathVariable UUID id) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(id);
    }
}
