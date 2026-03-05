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
@Table(name = "blocks")
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "blocked_id", nullable = false)
    private User blockedUser;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

     @PrePersist
    public void prePersist() {
        this.createdAt = java.time.LocalDateTime.now();
    }

    public Block() {
    }

    public Block(Long id, User user, User blockedUser) {
        this.id = id;
        this.user = user;
        this.blockedUser = blockedUser;
    }

    public Long getId(){return id;}

    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public User getBlockedUser() { return blockedUser; }
    public void setBlockedUser(User blockedUser) { this.blockedUser = blockedUser; }
}
