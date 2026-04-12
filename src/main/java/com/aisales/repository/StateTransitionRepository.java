package com.aisales.repository;

import com.aisales.domain.model.Conversation;
import com.aisales.domain.model.StateTransition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StateTransitionRepository extends JpaRepository<StateTransition, UUID> {

    List<StateTransition> findByConversationOrderByCreatedAtAsc(Conversation conversation);

    List<StateTransition> findByConversationIdOrderByCreatedAtAsc(UUID conversationId);
}
