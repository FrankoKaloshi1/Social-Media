package com.example.SocialMedia.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.SocialMedia.Dto.FollowerResponseDto;
import com.example.SocialMedia.Dto.UserSummaryDto;
import com.example.SocialMedia.model.Follower;
import com.example.SocialMedia.model.User;
import com.example.SocialMedia.repositories.FollowerRepository;
import com.example.SocialMedia.repositories.UserRepository;

@Service
public class FollowService {
    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;
    private final BlockService blockService;

    public FollowService(FollowerRepository followerRepository, UserRepository userRepository, BlockService blockService) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
        this.blockService = blockService; 
    }

    public FollowerResponseDto followUser(Long userId, Long followingId) {
        if (userId.equals(followingId)) {
            throw new IllegalArgumentException("Users cannot follow themselves.");
        }

        var followerUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Follower user not found with id: " + userId));
        var followingUser = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("Following user not found with id: " + followingId));


        if (blockService.isBlocked(userId, followingId)) {
            throw new IllegalArgumentException("Cannot follow user with id: " + followingId + " because you have blocked them.");
        }

        Follower follower = new Follower();
        follower.setFollowerUser(followerUser);
        follower.setFollowingUser(followingUser);
        followerRepository.save(follower);

        return new FollowerResponseDto(
            new UserSummaryDto(followerUser.getName(), followerUser.getSurname(), followerUser.getEmail()),
            new UserSummaryDto(followingUser.getName(), followingUser.getSurname(), followingUser.getEmail()),
            follower.isAccepted()
        );
    }

    public void acceptFollowRequest(Long userId, Long followerId) {
        var followRequest = followerRepository.findByFollowerUserIdAndFollowingUserId(followerId, userId)
                .orElseThrow(() -> new RuntimeException("Follow request not found for followerId: " + followerId + " and followingId: " + userId));

        followRequest.setAccepted(true);
        
        followerRepository.save(followRequest);
    }

    public void rejectFollowRequest(Long userId, Long followerId) {
        var followRequest = followerRepository.findByFollowerUserIdAndFollowingUserId(followerId, userId)
                .orElseThrow(() -> new RuntimeException("Follow request not found for followerId: " + followerId + " and followingId: " + userId));

        followerRepository.delete(followRequest);
    }

    public void unfollow(Long userId , Long followingId) {

        var follow = followerRepository.findByFollowerUserIdAndFollowingUserId(userId, followingId)
                .orElseThrow(() -> new RuntimeException("Follow relationship not found for userId: " + userId + " and followingId: " + followingId));

        followerRepository.delete(follow);
    
    }


    public List<User> getUserFollowers(Long userId) {

        return followerRepository.getUserFollowers(userId);
    }

    public List<User> getUserFollowing(Long userId) {
        return followerRepository.getFollowingByUser(userId);

    }
        
    }
