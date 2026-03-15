package com.example.SocialMedia.Dto;

import java.time.LocalDateTime;

public class ChatMessageResponseDto {

    private final Long id;
    private final String role;
    private final String message;
    private final SharedPostResponseDto postShared;
    private final LocalDateTime createdAt;

    public ChatMessageResponseDto(Long id, String role, String message, SharedPostResponseDto postShared, LocalDateTime createdAt) {
        this.id = id;
        this.role = role;
        this.message = message;
        this.postShared = postShared;
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

    public SharedPostResponseDto getPostShared() {
        return postShared;
    }
}
