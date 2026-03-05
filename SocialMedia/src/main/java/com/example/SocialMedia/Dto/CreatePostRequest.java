package com.example.SocialMedia.Dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public class CreatePostRequest {
    
    @NotBlank(message = "Content is required")
    @Size(max = 500, message = "Content must be less than 500 characters")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content != null ? content.trim() : null;
    }


    
    private List<Long> image_ids;

    public List<Long> getImage_ids() {
        return image_ids;
    }

    public void setImage_ids(List<Long> image_ids) {
        this.image_ids = image_ids;
    }

    
}
