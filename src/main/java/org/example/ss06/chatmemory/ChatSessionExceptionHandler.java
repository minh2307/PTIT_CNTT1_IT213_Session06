package org.example.ss06.chatmemory;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ChatSessionExceptionHandler {

    @ExceptionHandler(ChatConversationService.ChatClientNotConfiguredException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    Map<String, String> handleChatClientNotConfigured(
            ChatConversationService.ChatClientNotConfiguredException exception) {
        return Map.of("error", exception.getMessage());
    }

    @ExceptionHandler(ChatSessionService.ChatSessionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Map<String, String> handleNotFound(ChatSessionService.ChatSessionNotFoundException exception) {
        return Map.of("error", exception.getMessage());
    }
}
