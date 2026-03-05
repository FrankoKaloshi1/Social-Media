package com.example.SocialMedia.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SocialMedia.model.Comment;

import jakarta.transaction.Transactional;

public interface  CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findByPostId(Long postId);

    List<Comment> findByUserId(Long userId);

    @Transactional
    void deleteAllByUserId(Long userId, Long postId);
    
}
