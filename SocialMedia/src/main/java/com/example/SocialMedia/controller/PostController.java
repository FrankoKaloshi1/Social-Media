package com.example.SocialMedia.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SocialMedia.Dto.CreatePostRequest;
import com.example.SocialMedia.Dto.PostResponseDto;
import com.example.SocialMedia.Dto.UpdatePostRequest;
import com.example.SocialMedia.model.Post;
import com.example.SocialMedia.services.JwtService;
import com.example.SocialMedia.services.PostService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/posts")
public class PostController {
    
    private final PostService postService;
    private final JwtService jwtService;

    public PostController(PostService postService, JwtService jwtService) {
        this.postService = postService;
        this.jwtService = jwtService;
    }

    // Get your own posts
    @GetMapping("")
    public ResponseEntity<?> getMyPosts(@RequestHeader("Authorization") String authHeader) {
       try{
        String token = authHeader.substring(7);
        Long userId = jwtService.validateTokenAndGetUserId(token);

        List<Post> posts = postService.getPostsByUser(userId, userId);
        List<PostResponseDto> response = posts.stream().map(PostResponseDto::new).toList();

        return ResponseEntity.ok(response);
       } catch (Exception e) {
           return ResponseEntity.badRequest().body("Error retrieving posts for user: " + e.getMessage());
       }
    }

    @GetMapping("/user/{ownerId}")
    public ResponseEntity<?> getPostsByUser(@PathVariable Long ownerId, @RequestHeader("Authorization") String authHeader) {
       try{
        String token = authHeader.substring(7);
        Long viewerId = jwtService.validateTokenAndGetUserId(token);

        List<Post> posts = postService.getPostsByUser(ownerId, viewerId);
        List<PostResponseDto> response = posts.stream().map(PostResponseDto::new).toList();

        return ResponseEntity.ok(response);
       } catch (Exception e) {
           return ResponseEntity.badRequest().body("Error retrieving posts: " + e.getMessage());
       }
    }

    @GetMapping("/private")
    public ResponseEntity<?> getPrivatePosts(@RequestHeader("Authorization") String authHeader) {
       try{
        String token = authHeader.substring(7);
        Long userId = jwtService.validateTokenAndGetUserId(token);

        List<Post> posts = postService.getPrivatePosts(userId);
        List<PostResponseDto> response = posts.stream().map(PostResponseDto::new).toList();

        return ResponseEntity.ok(response);
       } catch (Exception e) {
           return ResponseEntity.badRequest().body("Error retrieving private posts: " + e.getMessage());
       }
    }
   
    
    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody CreatePostRequest request, @RequestHeader("Authorization") String authHeader) {
        try{
            String token = authHeader.substring(7);
            Long userId = jwtService.validateTokenAndGetUserId(token);

            Post createdPost = postService.create(request, userId);

            return ResponseEntity.ok(new PostResponseDto(createdPost));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating post: " + e.getMessage());
        }
    }
    
    @PutMapping("/{postId}")
    public ResponseEntity<?> update(@Valid @RequestBody UpdatePostRequest request, @PathVariable Long postId, @RequestHeader("Authorization") String authHeader) {
        try{
         String token = authHeader.substring(7);
         Long userId = jwtService.validateTokenAndGetUserId(token);

         Post updatedPost = postService.update(postId, userId, request);

         return ResponseEntity.ok(new PostResponseDto(updatedPost));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating post: " + e.getMessage());
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> delete(@PathVariable Long postId, @RequestHeader("Authorization") String authHeader) {
        try{
            String token = authHeader.substring(7);
            Long userId = jwtService.validateTokenAndGetUserId(token);

            postService.delete(postId , userId);

         return ResponseEntity.ok("Post deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting post: " + e.getMessage());
        }
    }
}
