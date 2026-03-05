package com.example.SocialMedia.Dto;

public class FollowerResponseDto {

    private UserSummaryDto followerUser;
    private UserSummaryDto followingUser;
    private boolean accepted;

    public FollowerResponseDto(UserSummaryDto followerUser, UserSummaryDto followingUser, boolean accepted) {
        this.followerUser = followerUser;
        this.followingUser = followingUser;
        this.accepted = accepted;
    }

    public UserSummaryDto getFollowerUser() { return followerUser; }
    public UserSummaryDto getFollowingUser() { return followingUser; }
    public boolean isAccepted() { return accepted; }
}
