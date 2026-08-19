package org.example.ss06.chatmemory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.UserMessage;


class ChatSessionServiceTest {

    @Test
    void keepsOnlyConfiguredMessageWindow() {
        ChatMemory memory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(4)
                .build();

        memory.add("conversation-1", List.of(
                new UserMessage("one"),
                new UserMessage("two"),
                new UserMessage("three"),
                new UserMessage("four"),
                new UserMessage("five"),
                new UserMessage("six")));

        assertEquals(4, memory.get("conversation-1").size());
        assertEquals("three", memory.get("conversation-1").get(0).getText());
    }
}
