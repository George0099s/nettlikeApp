package com.avla.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostComsPayload {

    @SerializedName("post")
    private Post post;

    @SerializedName("comments")
    private List<Comment> comments = null;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
