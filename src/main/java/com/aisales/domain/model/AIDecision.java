package com.aisales.domain.model;

import com.aisales.domain.enums.AIAction;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "ai_decisions",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_ai_decisions_message_id", columnNames = {"message_id"})
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIDecision {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false, unique = true)
    private Message message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AIAction action;

    private String parameters;

    private String reasoning;

    @Column(nullable = false)
    private boolean executed;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        this.executed = false;
    }
}
