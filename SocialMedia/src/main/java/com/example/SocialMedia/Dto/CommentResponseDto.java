package com.example.SocialMedia.Dto;

import com.example.SocialMedia.model.Comment;

public class CommentResponseDto {
    
    private Long id;
    private String text;
    private UserSummaryDto user;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
        if(comment.getUser() != null) {
            this.user = new UserSummaryDto(comment.getUser().getName(), comment.getUser().getSurname(), comment.getUser().getEmail());
        }
    }

    public Long getId() { return id; }
    public String getText() { return text; }
    public UserSummaryDto getUser() { return user; }
}
