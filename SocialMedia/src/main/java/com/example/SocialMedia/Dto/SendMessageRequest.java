package com.example.SocialMedia.Dto;

import jakarta.validation.constraints.AssertTrue;

public class SendMessageRequest {
    
    private Long receiverId;
    private String message;
    private Long postId;

    @AssertTrue(message = "Message is required when no post is shared")
    public boolean isMessageOrPostProvided() {
        return postId != null || (message != null && !message.isBlank());
    }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
}
