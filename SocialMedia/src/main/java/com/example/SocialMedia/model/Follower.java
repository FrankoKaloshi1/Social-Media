package com.example.SocialMedia.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "followers")
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User followerUser;

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private User followingUser;

    @Column(name = "accepted", nullable = false)
    private boolean accepted = false;

    
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

     @PrePersist
    public void prePersist() {
        this.createdAt = java.time.LocalDateTime.now();
    }

    public Follower() {
    }

    public Follower(Long id, User followerUser, User followingUser, boolean accepted) {
        this.id = id;
        this.followerUser = followerUser;
        this.followingUser = followingUser;
        this.accepted = accepted;
    }

    public Long getId() {return id;}
    public void setId(Long id){this.id = id;}

    public User getFollowerUser(){return followerUser;}
    public void setFollowerUser(User followerUser){this.followerUser = followerUser;}

    public User getFollowingUser(){return followingUser;}
    public void setFollowingUser(User followingUser){this.followingUser = followingUser;}



    
    public boolean isAccepted(){return accepted;}
    public void setAccepted(boolean accepted){this.accepted = accepted;}

    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
}
