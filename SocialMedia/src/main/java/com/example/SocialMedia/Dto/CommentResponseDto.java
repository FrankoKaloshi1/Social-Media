package com.example.SocialMedia.Dto;

import com.example.SocialMedia.model.Comment;

public class CommentResponseDto {
    
    private Long id;
    private String text;
    private Long postId;
    private UserSummaryDto user;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.postId = comment.getPost().getId();
       if(comment.getUser() != null) {
            this.user = new UserSummaryDto(comment.getUser().getName(), comment.getUser().getSurname(), comment.getUser().getEmail());
        }
    }

    public Long getId() { return id; }
    public String getText() { return text; }
    public Long getPostId() { return postId; }
    public UserSummaryDto getUser() { return user; }
}
