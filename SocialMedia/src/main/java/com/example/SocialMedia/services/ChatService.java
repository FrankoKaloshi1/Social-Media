package com.example.SocialMedia.services;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.SocialMedia.Dto.ChatMessageResponseDto;
import com.example.SocialMedia.Dto.DeleteMessagesRequest;
import com.example.SocialMedia.Dto.SendMessageRequest;
import com.example.SocialMedia.Dto.UpdateMessageRequest;
import com.example.SocialMedia.model.Chat;
import com.example.SocialMedia.model.User;
import com.example.SocialMedia.repositories.ChatRepository;
import com.example.SocialMedia.repositories.FollowerRepository;
import com.example.SocialMedia.repositories.UserRepository;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final BlockService blockService;
    private final FollowerRepository followerRepository;
    private final SecretKeySpec aesKey;


    public ChatService(ChatRepository chatRepository,
                       UserRepository userRepository,
                       BlockService blockService,
                       FollowerRepository followerRepository,
                       @Value("${chat.encryption.secret}") String secret) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.blockService = blockService;
        this.followerRepository = followerRepository;
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.aesKey = new SecretKeySpec(keyBytes, "AES");
    }

    public void sendMessage(Long userId, SendMessageRequest request) {
        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if(blockService.isBlocked(userId, receiver.getId())) {
            throw new IllegalArgumentException("Cannot send message to user with id: " + receiver.getId() + " because you have blocked them.");
        }

        if(followerRepository.findByFollowerUserIdAndFollowingUserId(userId, receiver.getId()).isEmpty()) {
            throw new IllegalArgumentException("Cannot send message to user with id: " + receiver.getId() + " because you are not following them.");
        }


        String encryptedMessage = encrypt(request.getMessage());

        Chat chat = new Chat();
        chat.setSender(sender);
        chat.setReceiver(receiver);
        chat.setMessage(encryptedMessage);
        chatRepository.save(chat);
    }

    public List<ChatMessageResponseDto> readMessages(Long userId, Long otherUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new RuntimeException("Other user not found"));

        List<Chat> sentMessages = chatRepository.findBySenderIdAndReceiverId(user.getId(), otherUser.getId());
        List<Chat> receivedMessages = chatRepository.findByReceiverIdAndSenderId(user.getId(), otherUser.getId());

        List<ChatMessageResponseDto> messages = new ArrayList<>();

        sentMessages.forEach(chat -> {
            if(chat.getUserDeleted() != null && chat.getUserDeleted().getId().equals(userId)) {
                return;
            }
            messages.add(new ChatMessageResponseDto(
                chat.getId(),
                "Sender",
                decrypt(chat.getMessage()),
                chat.getCreatedAt()));
            });

        receivedMessages.forEach(chat -> 
            {
            if(chat.getUserDeleted() != null && chat.getUserDeleted().getId().equals(userId)) {
                return;
            }
            chat.setIsRead(true);
            chatRepository.save(chat);
            messages.add(new ChatMessageResponseDto(
                    chat.getId(),
                    "Receiver",
                    decrypt(chat.getMessage()),
                    chat.getCreatedAt()));
        });
        messages.sort(Comparator.comparing(ChatMessageResponseDto::getCreatedAt));
        return messages;
    }

    public Chat  editMessage(Long chatId, Long userId, UpdateMessageRequest request) {
        LocalDateTime now =LocalDateTime.now();
        
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat message not found"));

        if(!chat.getSender().getId().equals(userId)) {
            throw new IllegalArgumentException("You can only edit your own messages.");
        }


        if(request.getNewMessage() == null || request.getNewMessage().isBlank()) {

          return chat;

        }

        if(now.isAfter(chat.getCreatedAt().plusMinutes(15))) {
            throw new IllegalArgumentException("Cannot edit message after 15 minutes of sending.");
        }


        chat.setMessage(encrypt(request.getNewMessage()));
        return chatRepository.save(chat);
    }

    public void deleteMessages(DeleteMessagesRequest request, Long userId) {
      if(request.isForEveryone() == true){
        this.bulkDeleteMessages(request.getMessageIds(), userId);
      }else{
        this.softDeleteMessages(request.getMessageIds(), userId);
      }
    }

    private void bulkDeleteMessages(List<Long> messageIds, Long userId) {
        for (Long messageId : messageIds) {
            Chat chat = chatRepository.findById(messageId)
                    .orElseThrow(() -> new RuntimeException("Chat message not found"));

            if (!chat.getSender().getId().equals(userId)) {
                throw new IllegalArgumentException("You can only delete your own messages.");
            }
            if(chat.getIsRead() == true){
                throw new IllegalArgumentException("Cannot delete message for everyone because it has already been read by the receiver.");
            }
            chatRepository.delete(chat);
        }
    }

    private void softDeleteMessages(List<Long> messageIds, Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        for (Long messageId : messageIds) {


            Chat chat = chatRepository.findById(messageId)
                    .orElseThrow(() -> new RuntimeException("Chat message not found"));
            
            if(chat.getUserDeleted() == user){
                throw new IllegalArgumentException("this message is already deleted for this user.");
            }

            if(chat.getUserDeleted() != null) {
               chatRepository.delete(chat);
               continue;
            }

            chat.setUserDeleted(user);
            chatRepository.save(chat);
        }
    }

     public String decrypt(String encryptedMessage) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            byte[] decoded = Base64.getDecoder().decode(encryptedMessage);
            return new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt message", e);
        }
    }

    private String encrypt(String message) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt message", e);
        }
    }

}
