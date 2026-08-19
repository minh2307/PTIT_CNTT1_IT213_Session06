package org.example.ss06.chatmemory;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat/sessions/{conversationId}/messages")
public class ChatConversationController {

    private final ChatConversationService conversationService;

    public ChatConversationController(ChatConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping
    public ResponseEntity<ChatTurnResponse> send(
            @PathVariable String conversationId,
            @Valid @RequestBody ChatTurnRequest request) {
        return ResponseEntity.ok(conversationService.send(conversationId, request.message()));
    }
}
