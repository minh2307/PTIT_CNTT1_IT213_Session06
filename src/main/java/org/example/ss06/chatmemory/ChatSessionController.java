package org.example.ss06.chatmemory;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat/sessions")
public class ChatSessionController {

    private final ChatSessionService sessionService;

    public ChatSessionController(ChatSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public ResponseEntity<ChatSessionService.ChatSession> create() {
        ChatSessionService.ChatSession session = sessionService.create();
        return ResponseEntity.created(URI.create("/api/chat/sessions/" + session.conversationId()))
                .body(session);
    }

    @GetMapping
    public List<ChatSessionService.ChatSession> findAll() {
        return sessionService.findAll();
    }

    @GetMapping("/{conversationId}")
    public ChatSessionService.ChatSession find(@PathVariable String conversationId) {
        return sessionService.find(conversationId);
    }

    @DeleteMapping("/{conversationId}")
    public ResponseEntity<Void> delete(@PathVariable String conversationId) {
        sessionService.delete(conversationId);
        return ResponseEntity.noContent().build();
    }
}
