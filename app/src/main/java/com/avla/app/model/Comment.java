package com.avla.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Comment {

    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("up_voted_by")
    @Expose
    private List<Object> upVotedBy = null;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("down_voted_by")
    @Expose
    private List<Object> downVotedBy = null;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("votes")
    @Expose
    private Integer votes;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("created_by")
    @Expose
    private CommCreatedBy createdBy;

    public String getCreatedAt() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            newFormat.setTimeZone(TimeZone.getDefault());
            date = df.parse(createdAt);
            this.createdAt = newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public List<Object> getUpVotedBy() {
        return upVotedBy;
    }

    public void setUpVotedBy(List<Object> upVotedBy) {
        this.upVotedBy = upVotedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }


    public CommCreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CommCreatedBy createdBy) {
        this.createdBy = createdBy;
    }

}

