package com.example.SocialMedia.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SocialMedia.model.Block;

public interface BlockRepository extends JpaRepository<Block, Long> {
    
    boolean existsByUserIdAndBlockedUserId(Long userId, Long blockedUserId);

    Optional<Block> findByUserIdAndBlockedUserId(Long userId, Long blockedUserId);
}
