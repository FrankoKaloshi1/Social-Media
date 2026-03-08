package com.example.SocialMedia.Dto;

import java.time.LocalDateTime;

public class ChatMessageResponseDto {

    private final Long id;
    private final String role;
    private final String message;
    private final LocalDateTime createdAt;

    public ChatMessageResponseDto(Long id, String role, String message, LocalDateTime createdAt) {
        this.id = id;
        this.role = role;
        this.message = message;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
