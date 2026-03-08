package com.example.SocialMedia.Dto;

import jakarta.validation.constraints.NotBlank;

public class SendMessageRequest {
    
    private Long receiverId;

    @NotBlank(message = "Message cannot be empty")
    private String message;

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
