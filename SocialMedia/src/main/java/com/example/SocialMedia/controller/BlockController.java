package com.example.SocialMedia.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SocialMedia.services.BlockService;
import com.example.SocialMedia.services.JwtService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/api/")
public class BlockController {

    private final BlockService blockService;
    private final JwtService jwtService;

    public BlockController(BlockService blockService, JwtService jwtService) {
        this.blockService = blockService;
        this.jwtService = jwtService;
    }


    @PostMapping("/block/{blockUserId}")
    public ResponseEntity<?> blockUser(@PathVariable Long blockUserId, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Long userId = jwtService.validateTokenAndGetUserId(token);

            blockService.blockUser(userId, blockUserId);
            return ResponseEntity.ok("Successfully blocked the user.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error blocking user: " + e.getMessage());
        }
    }

    @PostMapping("/unblock/{blockUserId}")
    public ResponseEntity<?> unblockUser(@PathVariable Long blockUserId, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Long userId = jwtService.validateTokenAndGetUserId(token);

            blockService.unblockUser(userId, blockUserId);
            return ResponseEntity.ok("Successfully unblocked the user.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error unblocking user: " + e.getMessage());
        }
    }

}