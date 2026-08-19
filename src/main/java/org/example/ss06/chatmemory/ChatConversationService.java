package org.example.ss06.chatmemory;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
public class ChatConversationService {

    private final ChatSessionService sessionService;
    private final ObjectProvider<ChatClient> chatClientProvider;
    private final MessageChatMemoryAdvisor chatMemoryAdvisor;
    private final ToolCallbackProvider dentalToolCallbackProvider;

    public ChatConversationService(
            ChatSessionService sessionService,
            ObjectProvider<ChatClient> chatClientProvider,
            MessageChatMemoryAdvisor chatMemoryAdvisor,
            ToolCallbackProvider dentalToolCallbackProvider) {
        this.sessionService = sessionService;
        this.chatClientProvider = chatClientProvider;
        this.chatMemoryAdvisor = chatMemoryAdvisor;
        this.dentalToolCallbackProvider = dentalToolCallbackProvider;
    }

    public ChatTurnResponse send(String conversationId, String message) {
        sessionService.find(conversationId);

        ChatClient chatClient = chatClientProvider.getIfAvailable();
        if (chatClient == null) {
            throw new ChatClientNotConfiguredException();
        }

        String response = chatClient.prompt()
                .user(message)
                .advisors(chatMemoryAdvisor)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
                .tools(dentalToolCallbackProvider)
                .call()
                .content();

        return new ChatTurnResponse(conversationId, response);
    }

    public static class ChatClientNotConfiguredException extends RuntimeException {
        public ChatClientNotConfiguredException() {
            super("ChatClient is not configured. Thành viên A must provide a ChatClient bean.");
        }
    }
}
