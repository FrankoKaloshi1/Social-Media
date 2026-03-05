package com.example.SocialMedia.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCommentRequest {

    @NotBlank(message = "Text must not be blank")
    private String text;

    @NotNull(message = "Post ID must not be null")
    private Long postId;

    public CreateCommentRequest() {}

    public CreateCommentRequest(String text, Long postId) {
        this.text = text;
        this.postId = postId;
    }

    public String getText() { return text; }
    public Long getPostId() { return postId; }
}
