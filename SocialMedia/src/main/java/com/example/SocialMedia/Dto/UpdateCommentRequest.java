package com.example.SocialMedia.Dto;

public class UpdateCommentRequest {

    
    private String text;

    public UpdateCommentRequest() {}

    public UpdateCommentRequest(String text) {
        this.text = text;
    }

    public String getText() { return text; }

}
