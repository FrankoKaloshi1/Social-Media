package com.example.SocialMedia.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SocialMedia.model.Image;


public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByPostId(Long postId);
}
