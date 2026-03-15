package com.example.SocialMedia.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.SocialMedia.Dto.SendMessageRequest;
import com.example.SocialMedia.services.JwtService;
import com.example.SocialMedia.services.ShareService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/share")
public class ShareController {

    private final ShareService shareService;
    private final JwtService jwtService;

    public ShareController(ShareService shareService, JwtService jwtService) {
        this.shareService = shareService;
        this.jwtService = jwtService;
    }

    @PostMapping({""})
    public ResponseEntity<?> sharePost(@Valid @RequestBody SendMessageRequest request, @RequestHeader("Authorization") String authHeader) {
        try{
            String token = authHeader.substring(7);
            Long userId = jwtService.validateTokenAndGetUserId(token);

            shareService.sharePost(userId, request);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error sharing post: " + e.getMessage());
        }

    }

    

}
