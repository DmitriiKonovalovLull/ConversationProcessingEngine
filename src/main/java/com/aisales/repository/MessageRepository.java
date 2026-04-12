package com.aisales.repository;

import com.aisales.domain.model.Conversation;
import com.aisales.domain.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findByConversationOrderByCreatedAtAsc(Conversation conversation);

    List<Message> findByConversationIdOrderByCreatedAtAsc(UUID conversationId);
}
