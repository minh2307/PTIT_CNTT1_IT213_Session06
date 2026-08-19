package org.example.ss06.chatmemory;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class ChatSessionService {

    private final ChatMemory chatMemory;
    private final ConcurrentMap<String, Instant> sessions = new ConcurrentHashMap<>();

    public ChatSessionService(ChatMemory chatMemory) {
        this.chatMemory = chatMemory;
    }

    public ChatSession create() {
        String conversationId = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();
        sessions.put(conversationId, createdAt);
        return details(conversationId);
    }

    public List<ChatSession> findAll() {
        return sessions.keySet().stream()
                .map(this::details)
                .sorted(Comparator.comparing(ChatSession::createdAt).reversed())
                .toList();
    }

    public ChatSession find(String conversationId) {
        if (!sessions.containsKey(conversationId)) {
            throw new ChatSessionNotFoundException(conversationId);
        }
        return details(conversationId);
    }

    public void delete(String conversationId) {
        if (sessions.remove(conversationId) == null) {
            throw new ChatSessionNotFoundException(conversationId);
        }
        chatMemory.clear(conversationId);
    }

    private ChatSession details(String conversationId) {
        return new ChatSession(
                conversationId,
                sessions.get(conversationId),
                chatMemory.get(conversationId).size());
    }

    public record ChatSession(String conversationId, Instant createdAt, int messageCount) {
    }

    public static class ChatSessionNotFoundException extends RuntimeException {
        public ChatSessionNotFoundException(String conversationId) {
            super("Chat session not found: " + conversationId);
        }
    }
}
