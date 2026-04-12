-- Create all tables if they don't exist (safe migration)
-- Order matters: conversations -> messages -> ai_decisions/state_transitions

-- 1. Conversations table (parent table, no FKs)
CREATE TABLE IF NOT EXISTS conversations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    channel VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    current_state VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE conversations
ADD COLUMN IF NOT EXISTS user_id VARCHAR(255);

CREATE INDEX IF NOT EXISTS idx_conversations_user_id ON conversations(user_id);
CREATE INDEX IF NOT EXISTS idx_conversations_user_channel ON conversations(user_id, channel);

-- 2. Messages table (FK to conversations)
CREATE TABLE IF NOT EXISTS messages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    conversation_id UUID NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
    role VARCHAR(50) NOT NULL,
    text TEXT NOT NULL,
    confidence_score DOUBLE PRECISION,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_messages_conversation_id ON messages(conversation_id);

-- 3. AI Decisions table (FK to messages, one-to-one)
CREATE TABLE IF NOT EXISTS ai_decisions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    message_id UUID NOT NULL UNIQUE REFERENCES messages(id) ON DELETE CASCADE,
    action VARCHAR(50) NOT NULL,
    parameters TEXT,
    reasoning TEXT,
    executed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_ai_decisions_message_id ON ai_decisions(message_id);

-- 4. State Transitions table (FK to conversations)
CREATE TABLE IF NOT EXISTS state_transitions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    conversation_id UUID NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
    from_state VARCHAR(255) NOT NULL,
    to_state VARCHAR(255) NOT NULL,
    reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_state_transitions_conversation_id ON state_transitions(conversation_id);
