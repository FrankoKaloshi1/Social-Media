package com.example.SocialMedia.Dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.SocialMedia.model.Post;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SharedPostResponseDto {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private UserSummaryDto user;
    private List<String> imageUrls;

    public SharedPostResponseDto(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        if (post.getUser() != null) {
            this.user = new UserSummaryDto(post.getUser().getName(), post.getUser().getSurname(), post.getUser().getEmail());
        }
        this.imageUrls = post.getImages() == null ? List.of() :
            post.getImages().stream().map(img -> img.getFileUrl()).collect(Collectors.toList());
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public UserSummaryDto getUser() { return user; }
    public List<String> getImageUrls() { return imageUrls; }
}
