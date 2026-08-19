package org.example.ss06.chatmemory;

import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Shared chat-memory configuration for the application.
 *
 * <p>The repository is intentionally in-memory for the exercise. Replacing it
 * with a JDBC-backed ChatMemoryRepository later does not require changing the
 * advisor or the REST API.</p>
 */
@Configuration
public class ChatMemoryConfiguration {

    @Bean
    ChatMemoryRepository chatMemoryRepository() {
        return new InMemoryChatMemoryRepository();
    }

    @Bean
    ChatMemory chatMemory(
            ChatMemoryRepository repository,
            @Value("${app.chat.memory.max-messages:20}") int maxMessages) {
        if (maxMessages < 2) {
            throw new IllegalArgumentException("app.chat.memory.max-messages must be at least 2");
        }

        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(repository)
                .maxMessages(maxMessages)
                .build();
    }

    /**
     * Add this advisor to the shared ChatClient. The conversation id is read
     * from ChatMemory.CONVERSATION_ID in each ChatClient request.
     */
    @Bean
    MessageChatMemoryAdvisor chatMemoryAdvisor(ChatMemory chatMemory) {
        return MessageChatMemoryAdvisor.builder(chatMemory).build();
    }
}
