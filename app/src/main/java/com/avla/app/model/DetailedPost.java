package com.avla.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailedPost {
    @SerializedName("post")
    private List<Post> posts;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
