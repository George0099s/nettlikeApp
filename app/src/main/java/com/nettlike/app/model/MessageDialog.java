package com.nettlike.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageDialog {
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

    public List<MessageMember> getMembers() {
        return members;
    }

    public void setMembers(List<MessageMember> members) {
        this.members = members;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    @SerializedName("_id")
    private String id;
    @SerializedName("status")
    private String status;
    @SerializedName("type")
    private String type;
    @SerializedName("members")
    private List<MessageMember> members = null;
    @SerializedName("created_by")
    private String createdBy;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("created_at")
    private String createdAt;
}
