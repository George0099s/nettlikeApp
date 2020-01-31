package com.avla.app.model.dialog;

import com.avla.app.model.LastMessage;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DiaPayload {
    @SerializedName("unread_count")
    private Integer unreadCount;
    @SerializedName("last_message")
    private LastMessage lastMessage;
    @SerializedName("created_by")
    private String createdBy;
    @SerializedName("_id")
    private String id;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("status")
    private String status;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("members")
    private List<DiaMember> members = null;
    @SerializedName("type")
    private String type;
    @SerializedName("dialog")
    private Dialog dialog;

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LastMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(LastMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<DiaMember> getMembers() {
        return members;
    }

    public void setMembers(List<DiaMember> members) {
        this.members = members;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }
}
