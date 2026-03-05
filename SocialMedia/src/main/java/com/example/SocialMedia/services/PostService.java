package com.example.SocialMedia.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.SocialMedia.Dto.CreatePostRequest;
import com.example.SocialMedia.Dto.UpdatePostRequest;
import com.example.SocialMedia.model.Comment;
import com.example.SocialMedia.model.Image;
import com.example.SocialMedia.model.Post;
import com.example.SocialMedia.model.User;
import com.example.SocialMedia.repositories.CommentRepository;
import com.example.SocialMedia.repositories.FollowerRepository;
import com.example.SocialMedia.repositories.ImageRepository;
import com.example.SocialMedia.repositories.PostRepository;
import com.example.SocialMedia.repositories.UserRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final FollowerRepository followerRepository;

    public PostService(PostRepository postRepository, ImageRepository imageRepository, UserRepository userRepository, CommentRepository commentRepository, FollowerRepository followerRepository) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.followerRepository = followerRepository;
    }

    public List<Post> getPostsByUser(Long ownerId, Long viewerId) {
        boolean isOwner = ownerId.equals(viewerId);
        boolean isFollowing = followerRepository.isFollowing(viewerId, ownerId);

        List<Post> userPosts = postRepository.findByUserId(ownerId);

        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + ownerId));


        if(isOwner){
            return postRepository.findByUserIdAndVisibility(ownerId, "public");
        }
        
        if(user.getProfileVisiblity().equals("private")){
            if(isFollowing){

                return postRepository.findByUserIdAndVisibility(ownerId, "public");
            }else{

                return null;            
            }
        }
         return postRepository.findByUserIdAndVisibility(ownerId, "public");
       
    }

    public List<Post> getPrivatePosts(Long userId) {

        return postRepository.findByUserIdAndVisibility(userId, "private");

    }


    public Post create (CreatePostRequest request, Long userId) {
        Post post = new Post();
        post.setContent(request.getContent());

        post.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId)));

        Post savedPost = postRepository.save(post);

        List<Long> imagesId = request.getImage_ids();

        if(imagesId == null || imagesId.isEmpty()) {
            return savedPost;
        }   

        for (Long imageId : imagesId) {
            Image image = imageRepository.findById(imageId)
                    .orElseThrow(() -> new RuntimeException("Image not found with id: " + imageId));
           image.setPost(savedPost);
           imageRepository.save(image);
        }

       return savedPost;
    }

    public Post update(Long postId, Long userId, UpdatePostRequest request) {

         Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("User not authorized to update this post");
        }

        if (request.getContent() != null && !request.getContent().trim().isEmpty()) {
            post.setContent(request.getContent());
            
        }
        postRepository.save(post);

        return post;
    }

    public void delete(Long postId , Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("User not authorized to delete this post");
        }

        List<Image> images = imageRepository.findByPostId(postId);
        List<Comment> comments = commentRepository.findByPostId(postId);
        for (Comment comment : comments) {
            commentRepository.delete(comment);
        }
        for (Image image : images) {
            imageRepository.delete(image);
        }

        postRepository.delete(post);
    }

}
