package com.example.SocialMedia.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SocialMedia.model.Chat;

public interface ChatRepository extends JpaRepository<Chat,Long>{
    
    List<Chat> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
    List<Chat> findByReceiverIdAndSenderId(Long receiverId, Long senderId);

}
