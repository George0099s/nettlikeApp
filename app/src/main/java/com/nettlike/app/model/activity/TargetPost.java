package com.nettlike.app.model.activity;

import android.os.Parcel;
import android.os.Parcelable;

import com.nettlike.app.model.CreatedBy;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TargetPost implements Parcelable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_by")
    @Expose
    private CreatedBy createdBy;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("lang")
    @Expose
    private String lang;
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
    @SerializedName("picture_url")
    @Expose
    private String pictureUrl;
    @SerializedName("comments_count")
    @Expose
    private Integer commentsCount;
    @SerializedName("saved_count")
    @Expose
    private Integer savedCount;

    protected TargetPost(Parcel in) {
        id = in.readString();
        status = in.readString();
        name = in.readString();
        description = in.readString();
        lang = in.readString();
        if (in.readByte() == 0) {
            votes = null;
        } else {
            votes = in.readInt();
        }
        updatedAt = in.readString();
        createdAt = in.readString();
        pictureUrl = in.readString();
        if (in.readByte() == 0) {
            commentsCount = null;
        } else {
            commentsCount = in.readInt();
        }
        if (in.readByte() == 0) {
            savedCount = null;
        } else {
            savedCount = in.readInt();
        }
    }

    public static final Creator<TargetPost> CREATOR = new Creator<TargetPost>() {
        @Override
        public TargetPost createFromParcel(Parcel in) {
            return new TargetPost(in);
        }

        @Override
        public TargetPost[] newArray(int size) {
            return new TargetPost[size];
        }
    };

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

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
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

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Integer getSavedCount() {
        return savedCount;
    }

    public void setSavedCount(Integer savedCount) {
        this.savedCount = savedCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(status);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(lang);
        if (votes == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(votes);
        }
        dest.writeString(updatedAt);
        dest.writeString(createdAt);
        dest.writeString(pictureUrl);
        if (commentsCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(commentsCount);
        }
        if (savedCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(savedCount);
        }
    }
}
