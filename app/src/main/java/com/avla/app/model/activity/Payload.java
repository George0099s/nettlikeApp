package com.avla.app.model.activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payload {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("account_id")
    @Expose
    private String accountId;
    @SerializedName("is_seen")
    @Expose
    private Boolean isSeen;
    @SerializedName("target_id")
    @Expose
    private String targetId;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("target_post")
    @Expose
    private TargetPost targetPost;
    @SerializedName("target_comment")
    @Expose
    private TargetComment targetComment;
    @SerializedName("target_event")
    @Expose
    private TargetEvent targetEvent;
    @SerializedName("target_account")
    @Expose
    private TargetFollower targetFollower;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Boolean getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(Boolean isSeen) {
        this.isSeen = isSeen;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public TargetPost getTargetPost() {
        return targetPost;
    }

    public void setTargetPost(TargetPost targetPost) {
        this.targetPost = targetPost;
    }

    public TargetComment getTargetComment() {
        return targetComment;
    }

    public void setTargetComment(TargetComment targetComment) {
        this.targetComment = targetComment;
    }

    public TargetEvent getTargetEvent() {
        return targetEvent;
    }

    public void setTargetEvent(TargetEvent targetEvent) {
        this.targetEvent = targetEvent;
    }

    public TargetFollower getTargetFollower() {
        return targetFollower;
    }

    public void setTargetFollower(TargetFollower targetFollower) {
        this.targetFollower = targetFollower;
    }
}
