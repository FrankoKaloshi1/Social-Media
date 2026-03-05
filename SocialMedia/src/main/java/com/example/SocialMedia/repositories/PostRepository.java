package com.example.SocialMedia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SocialMedia.model.Post;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> 
{
    List<Post> findByUserId(Long userId);

    List<Post> findByUserIdAndVisibility(Long userId, String visibility);
}
