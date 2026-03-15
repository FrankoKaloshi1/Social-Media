package com.example.SocialMedia.services;

import org.springframework.stereotype.Service;

import com.example.SocialMedia.Dto.SendMessageRequest;
import com.example.SocialMedia.model.Post;
import com.example.SocialMedia.repositories.PostRepository;

@Service
public class ShareService {

    private final PostRepository postRepository;
    private final ChatService chatService;

    public ShareService(PostRepository postRepository, ChatService chatService   ) {
        this.postRepository = postRepository;
        this.chatService = chatService;
    }
    
    public void sharePost(Long userId , SendMessageRequest request){

        if (request.getPostId() == null) {
            throw new IllegalArgumentException("postId is required for sharing a post");
        }

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + request.getPostId()));

        chatService.sendMessage(userId, request);

        int sharesCount = post.getSharesCount();
        post.setSharesCount(sharesCount + 1);
        postRepository.save(post);

    }

}
