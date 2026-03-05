package com.example.SocialMedia.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SocialMedia.Dto.CommentResponseDto;
import com.example.SocialMedia.Dto.CreateCommentRequest;
import com.example.SocialMedia.Dto.UpdateCommentRequest;
import com.example.SocialMedia.model.Comment;
import com.example.SocialMedia.services.CommentService;
import com.example.SocialMedia.services.JwtService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final JwtService jwtService;

    public CommentController(CommentService commentService, JwtService jwtService) {
        this.commentService = commentService;
        this.jwtService = jwtService;
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable Long postId) {
        try {
            List<Comment> comments = commentService.getCommentsByPostId(postId);
            return ResponseEntity.ok(comments.stream().map(CommentResponseDto::new).toList());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving comments for post: " + e.getMessage());
    }
}

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody CreateCommentRequest request, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Long userId = jwtService.validateTokenAndGetUserId(token);
            Comment comment = commentService.create(request, userId);
            return ResponseEntity.ok(new CommentResponseDto(comment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating comment: " + e.getMessage());
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> update(@PathVariable Long commentId, @Valid @RequestBody UpdateCommentRequest request, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Long userId = jwtService.validateTokenAndGetUserId(token);
            Comment comment = commentService.updateComment(commentId, request.getText(), userId);
            return ResponseEntity.ok(new CommentResponseDto(comment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating comment: " + e.getMessage());
        }
    }
}
