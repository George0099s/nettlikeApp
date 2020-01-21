package com.avla.app.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PayloadTag {
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
    private List<String> parentIds = null;
    @SerializedName("status")
    private String status;
    @SerializedName("updated_at")
    private String updatedAt;

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
    public List<String> getParentIds() {
        return parentIds;
    }

    @SerializedName("parent_ids")
    public void setParentIds(List<String> parentIds) {
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
}
