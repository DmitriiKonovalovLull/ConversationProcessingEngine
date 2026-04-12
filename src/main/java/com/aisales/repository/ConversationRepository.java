package com.aisales.repository;

import com.aisales.domain.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    Optional<Conversation> findByUserIdAndChannel(String userId, String channel);

    List<Conversation> findByUserId(String userId);
}
