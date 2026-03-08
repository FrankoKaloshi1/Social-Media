package com.example.SocialMedia.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SocialMedia.Dto.ChatMessageResponseDto;
import com.example.SocialMedia.Dto.DeleteMessagesRequest;
import com.example.SocialMedia.Dto.SendMessageRequest;
import com.example.SocialMedia.Dto.UpdateMessageRequest;
import com.example.SocialMedia.services.ChatService;
import com.example.SocialMedia.services.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;
    private final JwtService jwtService;

    public ChatController(ChatService chatService, JwtService jwtService) {
        this.chatService = chatService;
        this.jwtService = jwtService;
    }

    @PostMapping("")
    public ResponseEntity<?> sendMessage(@Valid @RequestBody SendMessageRequest request,
                                         @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Long userId = jwtService.validateTokenAndGetUserId(token);
            chatService.sendMessage(userId, request);
            return ResponseEntity.ok("Message sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending message: " + e.getMessage());
        }
    }

    @GetMapping("/{otherUserId}")
    public ResponseEntity<?> readMessages(@PathVariable Long otherUserId,
                                          @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Long userId = jwtService.validateTokenAndGetUserId(token);
            List<ChatMessageResponseDto> messages = chatService.readMessages(userId, otherUserId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error reading messages: " + e.getMessage());
        }
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<?> updateMessage(@PathVariable Long messageId,
                                           @Valid @RequestBody UpdateMessageRequest request,
                                           @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Long userId = jwtService.validateTokenAndGetUserId(token);
            chatService.editMessage(messageId, userId, request);
            return ResponseEntity.ok("Message updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating message: " + e.getMessage());
        }
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteMessage(@Valid @RequestBody DeleteMessagesRequest request,
                                           @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Long userId = jwtService.validateTokenAndGetUserId(token);
            chatService.deleteMessages(request, userId);
            return ResponseEntity.ok("Messages deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting messages: " + e.getMessage());
        }
    }
}
