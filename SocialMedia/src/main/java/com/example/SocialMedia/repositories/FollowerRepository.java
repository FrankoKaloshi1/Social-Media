package com.example.SocialMedia.repositories;

import com.example.SocialMedia.model.Follower;
import com.example.SocialMedia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {

    @Query("SELECT followingUser FROM Follower  WHERE followerUser.id = :userId AND accepted = true")
    List<User> getFollowingByUser(@Param("userId") Long userId);

    @Query("SELECT followerUser FROM Follower  WHERE followingUser.id = :userId AND accepted = true")
    List<User> getUserFollowers(@Param("userId") Long userId);

    @Query("SELECT followerUser FROM Follower  WHERE followingUser.id = :userId AND accepted = false")
    List<User> getPendingFollowRequests(@Param("userId") Long userId);

    @Query("SELECT COUNT(f) > 0 FROM Follower f WHERE f.followerUser.id = :followerId AND f.followingUser.id = :followingId AND f.accepted = true")
    boolean isFollowing(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    Optional<Follower> findByFollowerUserIdAndFollowingUserId(Long followerId, Long followingId);

    @Transactional
    void deleteAllByFollowerUserIdAndFollowingUserId(Long followerId, Long followingId);

}
