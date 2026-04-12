package com.aisales.repository;

import com.aisales.domain.model.AIDecision;
import com.aisales.domain.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AIDecisionRepository extends JpaRepository<AIDecision, UUID> {

    Optional<AIDecision> findByMessage(Message message);

    Optional<AIDecision> findByMessageId(UUID messageId);
}
