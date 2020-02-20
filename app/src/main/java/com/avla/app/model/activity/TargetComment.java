package com.avla.app.model.activity;

import com.avla.app.model.CreatedBy;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TargetComment {
    @SerializedName("created_by")
    @Expose
    private CreatedBy createdBy;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("votes")
    @Expose
    private Integer votes;
    @SerializedName("up_voted_by")
    @Expose
    private List<Object> upVotedBy = null;
    @SerializedName("down_voted_by")
    @Expose
    private List<Object> downVotedBy = null;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public List<Object> getUpVotedBy() {
        return upVotedBy;
    }

    public void setUpVotedBy(List<Object> upVotedBy) {
        this.upVotedBy = upVotedBy;
    }

    public List<Object> getDownVotedBy() {
        return downVotedBy;
    }

    public void setDownVotedBy(List<Object> downVotedBy) {
        this.downVotedBy = downVotedBy;
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

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }
}
