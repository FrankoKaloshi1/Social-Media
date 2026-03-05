package com.example.SocialMedia.services;

import java.util.List;

import org.springframework.stereotype.Service;
import com.example.SocialMedia.model.Block;
import com.example.SocialMedia.model.Post;
import com.example.SocialMedia.repositories.BlockRepository;
import com.example.SocialMedia.repositories.CommentRepository;
import com.example.SocialMedia.repositories.FollowerRepository;
import com.example.SocialMedia.repositories.PostRepository;
import com.example.SocialMedia.repositories.UserRepository;

@Service
public class BlockService {

    private final BlockRepository blockRepository;
    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public BlockService(BlockRepository blockRepository, FollowerRepository followerRepository, UserRepository userRepository, CommentRepository commentRepository, PostRepository postRepository) {
        this.blockRepository = blockRepository;
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public void blockUser(Long userId , Long blockedUserId) {
        if (userId.equals(blockedUserId)) {
            throw new IllegalArgumentException("Users cannot block themselves.");
        }

        if (blockRepository.existsByUserIdAndBlockedUserId(userId, blockedUserId)) {
            throw new IllegalArgumentException("You have already blocked user with id: " + blockedUserId);
        }

       if(this.isBlocked(blockedUserId, userId)){
            throw new IllegalArgumentException("Cannot block user with id: " + blockedUserId + " because they have blocked you.");
        }

        this.deleteAllCommentOfBlockedUser(userId, blockedUserId);

        Block block = new Block();

        block.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId)));
        block.setBlockedUser(userRepository.findById(blockedUserId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + blockedUserId)));
        blockRepository.save(block);

        // Remove all follow relationships between both users
        followerRepository.deleteAllByFollowerUserIdAndFollowingUserId(userId, blockedUserId);
        followerRepository.deleteAllByFollowerUserIdAndFollowingUserId(blockedUserId, userId);

    }

    public void unblockUser(Long userId , Long blockedUserId) {
        var block = blockRepository.findByUserIdAndBlockedUserId(userId, blockedUserId)
                .orElseThrow(() -> new RuntimeException("Block not found for userId: " + userId + " and blockedUserId: " + blockedUserId));

        blockRepository.delete(block);
    }

    public boolean isBlocked(Long userId, Long otherUserId) {
        return blockRepository.existsByUserIdAndBlockedUserId(userId, otherUserId) ||
               blockRepository.existsByUserIdAndBlockedUserId(otherUserId, userId);
    }

    private void deleteAllCommentOfBlockedUser(Long userId, Long blockedUserId) {

        List<Post> ownedPosts = postRepository.findByUserId(userId);

        for(Post post : ownedPosts){
            commentRepository.deleteAllByUserId(blockedUserId, post.getId());
        }
    }
}
