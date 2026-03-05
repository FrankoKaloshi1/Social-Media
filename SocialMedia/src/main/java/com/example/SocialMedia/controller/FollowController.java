package com.example.SocialMedia.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.SocialMedia.Dto.FollowerResponseDto;
import com.example.SocialMedia.Dto.UserSummaryDto;
import com.example.SocialMedia.model.User;
import com.example.SocialMedia.services.FollowService;
import com.example.SocialMedia.services.JwtService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/follows")

public class FollowController {

    private final FollowService followService;
    private final JwtService jwtService;

    public FollowController(FollowService followService, JwtService jwtService) {
        this.followService = followService;
        this.jwtService = jwtService;
    }

    @PostMapping("/{followingId}")
    public ResponseEntity<?> followUser(@PathVariable Long followingId, @RequestHeader("Authorization") String authHeader)
    {
        try {
        String token = authHeader.substring(7);
        Long userId = jwtService.validateTokenAndGetUserId(token);

        FollowerResponseDto follow = followService.followUser(userId, followingId);
        return ResponseEntity.ok(follow);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error following user: " + e.getMessage());
        }
    }

    @DeleteMapping("/{followingId}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long followingId , @RequestHeader("Authorization") String authHeader){

        try{
            String token = authHeader.substring(7);
            Long userId = jwtService.validateTokenAndGetUserId(token);
            followService.unfollow(userId , followingId);
            return ResponseEntity.ok("Successfully unfollowed the user.");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error unfollowing user: " + e.getMessage());
        }
    }

    @PostMapping("/accept/{followerId}")
    public ResponseEntity<?> acceptFollowRequest(@PathVariable Long followerId , @RequestHeader("Authorization") String authHeader){
        try{
            String token = authHeader.substring(7);
            Long userId = jwtService.validateTokenAndGetUserId(token);
            followService.acceptFollowRequest(userId, followerId);
            return ResponseEntity.ok("Follow request accepted.");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error accepting follow request: " + e.getMessage());
        }
    }

        @DeleteMapping("/reject/{followerId}")
        public ResponseEntity<?> rejectFollowRequest(@PathVariable Long followerId , @RequestHeader("Authorization") String authHeader){
            try{
                String token = authHeader.substring(7);
                Long userId = jwtService.validateTokenAndGetUserId(token);
                followService.rejectFollowRequest(userId, followerId);
                return ResponseEntity.ok("Follow request rejected.");
            }catch (Exception e) {
                return ResponseEntity.badRequest().body("Error rejecting follow request: " + e.getMessage());
            }
        }

        @GetMapping("/user-followers")
        public ResponseEntity<?> getFollowers(@RequestHeader("Authorization") String authHeader) {
            try {
                String token = authHeader.substring(7);
                Long userId = jwtService.validateTokenAndGetUserId(token);
                List<User> followers = followService.getUserFollowers(userId);
                return ResponseEntity.ok(followers.stream().map(user -> new UserSummaryDto(user.getName(), user.getSurname(), user.getEmail())));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error retrieving followers: " + e.getMessage());
            }
        }

        @GetMapping("/user-following")
        public ResponseEntity<?> getFollowing(@RequestHeader("Authorization") String authHeader) {
            try {
                String token = authHeader.substring(7);
                Long userId = jwtService.validateTokenAndGetUserId(token);
                List<User> following = followService.getUserFollowing(userId);
                return ResponseEntity.ok(following.stream().map(user -> new UserSummaryDto(user.getName(), user.getSurname(), user.getEmail())));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error retrieving following: " + e.getMessage());
            }
        }
        
}
