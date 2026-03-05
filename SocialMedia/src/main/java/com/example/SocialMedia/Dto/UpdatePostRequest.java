package com.example.SocialMedia.Dto;

import jakarta.validation.constraints.Size;

public class UpdatePostRequest {
    
   @Size(max = 500, message = "Content must be less than 500 characters")

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content != null ? content.trim() : null;
    }
}