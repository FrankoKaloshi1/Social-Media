package com.example.SocialMedia.Dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.SocialMedia.model.Comment;
import com.example.SocialMedia.model.Image;
import com.example.SocialMedia.model.Post;

public class PostResponseDto {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private UserSummaryDto user;
    private List<Image> images;
    private List<Comment> comments;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.images = post.getImages();
        this.comments = post.getComments();
        if (post.getUser() != null) {
            this.user = new UserSummaryDto(post.getUser().getName(), post.getUser().getSurname() , post.getUser().getEmail());
        }
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public UserSummaryDto getUser() { return user; }
    public List<?> getImages() { return images; }
    public List<?> getComments() { return comments; }
}
