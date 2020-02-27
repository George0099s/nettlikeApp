package com.nettlike.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Post implements Parcelable {
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("tags")
    @Expose
    private List<PayloadTag> tags = new ArrayList<>();
    @SerializedName("comments_count")
    @Expose
    private String commentsCount;
    @SerializedName("down_voted_by")
    @Expose
    private List<Object> downVotedBy = null;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("saved_count")
    @Expose
    private int savedCount;
    @SerializedName("up_voted_by")
    @Expose
    private List<Object> upVotedBy = null;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("created_by")
    @Expose
    private CreatedBy createdBy;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("votes")
    @Expose
    private String votes;
    @SerializedName("post")
    @Expose
    private Post post;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments;
    @SerializedName("picture_url")
    @Expose
    private String pictureUrl;


    protected Post(Parcel in) {
        createdAt = in.readString();
        updatedAt = in.readString();
        name = in.readString();
        commentsCount = in.readString();
        lang = in.readString();
        savedCount = in.readInt();
        description = in.readString();
        id = in.readString();
        status = in.readString();
        votes = in.readString();
        post = in.readParcelable(Post.class.getClassLoader());
        pictureUrl = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PayloadTag> getTags() {
        return tags;
    }

    public void setTags(List<PayloadTag> tags) {
        this.tags = tags;
    }

    public String getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(String commentsCount) {
        this.commentsCount = commentsCount;
    }

    public List<Object> getDownVotedBy() {
        return downVotedBy;
    }

    public void setDownVotedBy(List<Object> downVotedBy) {
        this.downVotedBy = downVotedBy;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getSavedCount() {
        return savedCount;
    }

    public void setSavedCount(int savedCount) {
        this.savedCount = savedCount;
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

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

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

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }



    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(name);
        dest.writeString(commentsCount);
        dest.writeString(lang);
        dest.writeInt(savedCount);
        dest.writeString(description);
        dest.writeString(id);
//        dest.writeArray(tags);
        dest.writeString(status);
        dest.writeString(votes);
        dest.writeParcelable(post, flags);
        dest.writeString(pictureUrl);
    }
}
