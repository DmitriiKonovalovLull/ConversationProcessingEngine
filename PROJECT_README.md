# Conversation Processing Engine - Project Documentation

## Overview
Prototype conversation processing engine built with Spring Boot 3.2.0 and Java 21. Features async pipeline processing, AI integration, rate limiting, metrics collection, and channel abstraction.

## Tech Stack
- **Java**: 21
- **Spring Boot**: 3.2.0
- **Database**: PostgreSQL
- **Build Tool**: Maven
- **Migration**: Flyway
- **Metrics**: Micrometer + Actuator

## Project Structure (37 Java Files)

### Main Application
```
com.aisales/
├── ConversationProcessingEngineApplication.java  - Spring Boot entry point
```

### API Layer (5 files)
```
api/
├── MessageController.java          - POST /messages/incoming, POST /messages/send
├── ConversationController.java     - GET /conversations, GET /conversations/{id}/messages
└── dto/
    ├── IncomingMessageRequest.java - @NotBlank validation
    └── SendMessageRequest.java     - @NotBlank validation
```

### Application Services (3 files)
```
application/
├── ConversationService.java        - findOrCreateConversation, updateState
├── MessageService.java             - saveMessage, saveIncomingMessage, getHistory
└── DecisionService.java            - processDecision, markAsExecuted
```

### Configuration (3 files)
```
config/
├── AsyncConfig.java                - @EnableAsync
├── WebClientConfig.java            - OpenAI WebClient bean with auth headers
└── DbCheck.java                    - Database connection validation on startup
```

### Domain Models (7 files)
```
domain/
├── model/
│   ├── Conversation.java           - UUID, channel, userId, currentState, timestamps
│   ├── Message.java                - @ManyToOne Conversation (LAZY), role, text, confidenceScore
│   ├── AIDecision.java             - @OneToOne Message (LAZY), action, parameters, reasoning
│   └── StateTransition.java        - @ManyToOne Conversation (LAZY), fromState, toState
└── enums/
    ├── AIAction.java               - REPLY, TRANSFER_TO_HUMAN, SCHEDULE_CALLBACK, etc.
    ├── ConversationState.java      - NEW, GREETING, QUALIFICATION, PRESENTATION, etc.
    └── MessageRole.java            - USER, ASSISTANT, SYSTEM
```

### Integration Layer (7 files)
```
integration/
├── ai/
│   ├── AIClient.java               - Interface: analyzeMessage(String)
│   ├── MockAIClient.java           - Returns "INTERESTED"
│   └── OpenAIClient.java           - WebClient integration with gpt-3.5-turbo
└── channel/
    ├── MessageSender.java          - Interface: send(channel, userId, text)
    ├── MockSender.java             - Logs to console
    ├── TelegramSender.java         - Filters for "telegram" channel
    └── WhatsappSender.java         - Filters for "whatsapp" channel
```

### Metrics (1 file)
```
metrics/
└── MetricsCollector.java           - messages.incoming, messages.processed, pipeline.errors, ai.calls, ai.errors, ratelimit.hits
```

### Pipeline (8 files)
```
pipeline/
├── PipelineContext.java            - Data class: channel, userId, messageText, conversation, message, decision, aiAnalysisResult
├── MessagePipeline.java            - @Async process() with 5 steps
└── steps/
    ├── PipelineStep.java           - Interface: execute(context)
    ├── ConversationStep.java       - findOrCreateConversation
    ├── SaveMessageStep.java        - saveIncomingMessage
    ├── AIProcessingStep.java     - aiClient.analyzeMessage
    ├── DecisionStep.java           - determineAction + processDecision
    └── ResponseStep.java           - generateResponse + messageSender.send
```

### Repositories (4 files)
```
repository/
├── ConversationRepository.java     - findByUserIdAndChannel, findByUserId
├── MessageRepository.java        - findByConversationOrderByCreatedAtAsc, findByConversationIdOrderByCreatedAtAsc
├── AIDecisionRepository.java     - findByMessage, findByMessageId
└── StateTransitionRepository.java  - findByConversationOrderByCreatedAtAsc, findByConversationIdOrderByCreatedAtAsc
```

### Security (1 file)
```
security/
└── RateLimiter.java                - 50 requests/user, ConcurrentHashMap
```

## API Endpoints

### Message Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/messages/incoming` | Incoming message with rate limiting |
| POST | `/messages/send` | Send message through pipeline |

### Conversation Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/conversations?userId={id}` | List user conversations |
| GET | `/conversations/{id}/messages` | Get conversation history |

### Management Endpoints (Actuator)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/actuator/health` | Health check |
| GET | `/actuator/metrics` | Application metrics |
| GET | `/actuator/prometheus` | Prometheus format |

## Pipeline Flow
```
1. ConversationStep    → Find/create conversation
2. SaveMessageStep     → Save incoming message to DB
3. AIProcessingStep    → Analyze with AI (Mock or OpenAI)
4. DecisionStep        → Determine action from AI result
5. ResponseStep        → Generate and send response
```

## Configuration (application.yml)
```yaml
- Database: Neon PostgreSQL @ ep-cold-mud-adz24lx5-pooler.c-2.us-east-1.aws.neon.tech:5432/neondb
- Server Port: 8080
- JPA: ddl-auto=validate, show-sql=true
- Flyway: enabled, baseline-on-migrate=true, location=classpath:db/migration
- Management: health, info, metrics, prometheus exposed
- Logging: INFO level for com.aisales
- OpenAI: Configurable via env var OPENAI_KEY or openai.api-key property
```

## Dependencies (pom.xml)
- spring-boot-starter-web
- spring-boot-starter-webflux (WebClient)
- spring-boot-starter-data-jpa
- postgresql (runtime)
- spring-boot-starter-validation
- spring-boot-starter-actuator
- flyway-core
- lombok

## Entity Relationships
```
Conversation (1) ←→ (N) Message
Message (1) ←→ (1) AIDecision
Conversation (1) ←→ (N) StateTransition
```

## Key Design Decisions
1. **Async Processing**: @Async on MessagePipeline.process() for non-blocking
2. **Rate Limiting**: 50 requests per userId via ConcurrentHashMap
3. **Lazy Loading**: All entity relationships use FetchType.LAZY
4. **Interface-based**: AIClient and MessageSender are interfaces for easy swapping
5. **Pipeline Pattern**: 5 discrete steps for message processing
6. **DTO Validation**: @NotBlank on all API inputs

## Issues Fixed
1. **DecisionService.java** - Fixed `messageId` builder field (changed to `message` entity lookup via MessageRepository)
2. **Database migrations** - Added Flyway V1__init_schema.sql with all tables and indexes

## Current Status
All 38 Java files have:
- Correct imports
- Proper annotations
- Consistent Lombok usage
- Valid Spring annotations
- Correct method signatures
- No syntax errors
- No logic errors detected

## File Count Summary
- Java files: 38
- Config files: 3 (pom.xml, application.yml, V1__init_schema.sql)
- Total lines of Java code: ~850+

## Database Schema
Flyway migration creates 4 tables:
- `conversations` - chat sessions
- `messages` - incoming/outgoing messages
- `ai_decisions` - AI analysis results
- `state_transitions` - conversation state history

## Next Steps for Production
1. Configure OpenAI API key via environment variable (OPENAI_KEY)
2. Add real Telegram/WhatsApp sender implementations
3. Add authentication/authorization (JWT/API keys)
4. Add error handling middleware
5. Configure logging to external system (ELK/Splunk)
6. Add monitoring alerts
7. Performance testing
