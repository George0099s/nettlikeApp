package com.avla.app.model;

import com.google.gson.annotations.SerializedName;



import java.util.List;

public class PostPayload {
    @SerializedName("posts")
    private List<Post> posts = null;

    @SerializedName("post")
    private Post post;





    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }


    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
