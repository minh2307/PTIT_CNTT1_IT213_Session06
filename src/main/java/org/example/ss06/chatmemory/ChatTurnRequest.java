package org.example.ss06.chatmemory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatTurnRequest(
        @NotBlank(message = "message must not be blank")
        @Size(max = 4_000, message = "message must not exceed 4000 characters")
        String message) {
}
