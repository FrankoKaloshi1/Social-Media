package com.example.SocialMedia.Dto;

import java.util.List;

public class DeleteMessagesRequest {

    // null or empty = delete all messages in the conversation
    private List<Long> messageIds;

    // true = delete for everyone, false = delete only for me
    private boolean forEveryone;

    public List<Long> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(List<Long> messageIds) {
        this.messageIds = messageIds;
    }

    public boolean isForEveryone() {
        return forEveryone;
    }

    public void setForEveryone(boolean forEveryone) {
        this.forEveryone = forEveryone;
    }
}
