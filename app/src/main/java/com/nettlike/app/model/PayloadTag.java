package com.nettlike.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PayloadTag implements Parcelable {
    @SerializedName("_id")
    private String id;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("emoji")
    private String emoji;
    @SerializedName("lang")
    private String lang;
    @SerializedName("name")
    private String name;
    @SerializedName("order")
    private Integer order;
    @SerializedName("parent_ids")
    private ArrayList<String> parentIds = null;
    @SerializedName("status")
    private String status;
    @SerializedName("updated_at")
    private String updatedAt;

    public static final Creator<PayloadTag> CREATOR = new Creator<PayloadTag>() {
        @Override
        public PayloadTag createFromParcel(Parcel in) {
            return new PayloadTag(in);
        }

        @Override
        public PayloadTag[] newArray(int size) {
            return new PayloadTag[size];
        }
    };

    protected PayloadTag(Parcel in) {
        id = in.readString();
        createdAt = in.readString();
        emoji = in.readString();
        lang = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            order = null;
        } else {
            order = in.readInt();
        }
        parentIds = in.createStringArrayList();
        status = in.readString();
        updatedAt = in.readString();
    }

    @SerializedName("_id")
    public String getId() {
        return id;
    }

    @SerializedName("_id")
    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @SerializedName("created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @SerializedName("emoji")
    public String getEmoji() {
        return emoji;
    }

    @SerializedName("emoji")
    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    @SerializedName("lang")
    public String getLang() {
        return lang;
    }

    @SerializedName("lang")
    public void setLang(String lang) {
        this.lang = lang;
    }

    @SerializedName("name")
    public String getName() {
        return name;
    }

    @SerializedName("name")
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("order")
    public Integer getOrder() {
        return order;
    }

    @SerializedName("order")
    public void setOrder(Integer order) {
        this.order = order;
    }

    @SerializedName("parent_ids")
    public ArrayList<String> getParentIds() {
        return parentIds;
    }

    @SerializedName("parent_ids")
    public void setParentIds(ArrayList<String> parentIds) {
        this.parentIds = parentIds;
    }

    @SerializedName("status")
    public String getStatus() {
        return status;
    }

    @SerializedName("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("updated_at")
    public String getUpdatedAt() {
        return updatedAt;
    }

    @SerializedName("updated_at")
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(createdAt);
        dest.writeString(emoji);
        dest.writeString(lang);
        dest.writeString(name);
        if (order == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(order);
        }
        dest.writeStringList(parentIds);
        dest.writeString(status);
        dest.writeString(updatedAt);
    }
}
