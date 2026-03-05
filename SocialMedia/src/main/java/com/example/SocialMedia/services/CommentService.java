package com.example.SocialMedia.services;

import java.util.List;

import com.example.SocialMedia.Dto.CreateCommentRequest;
import com.example.SocialMedia.model.Comment;
import com.example.SocialMedia.model.Post;
import com.example.SocialMedia.model.User;
import com.example.SocialMedia.repositories.CommentRepository;
import com.example.SocialMedia.repositories.PostRepository;
import com.example.SocialMedia.repositories.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository; 
    private final BlockService blockService;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository, BlockService blockService) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.blockService = blockService;
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }


    public Comment create(CreateCommentRequest request, Long userId) {

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + request.getPostId()));

        User postOwner = post.getUser();

        if(blockService.isBlocked(userId, postOwner.getId())) {
            throw new RuntimeException("You are blocked by the post owner.");
        }

        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setPost(postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + request.getPostId())));
        comment.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId)));
        return commentRepository.save(comment);

    }

    public Comment updateComment (Long commentId, String newText, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));

        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not the owner of this comment.");
        }
        comment.setText(newText);
        return commentRepository.save(comment);
    }

    public void deleteComment (Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));    
        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not the owner of this comment.");
        }
        commentRepository.delete(comment);
    }
}
