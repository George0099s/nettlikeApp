package com.nettlike.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventPayload {
    @SerializedName("events")
    @Expose
    private List<Event> events = null;

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("youtube_url")
    @Expose
    private String youtubeUrl;
    @SerializedName("first_button_text")
    @Expose
    private String firstButtonText;
    @SerializedName("first_button_url")
    @Expose
    private String firstButtonUrl;
    @SerializedName("second_button_text")
    @Expose
    private String secondButtonText;
    @SerializedName("second_button_url")
    @Expose
    private String secondButtonUrl;
    @SerializedName("photos")
    @Expose
    private List<Object> photos = null;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("saved_count")
    @Expose
    private Integer savedCount;

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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getFirstButtonText() {
        return firstButtonText;
    }

    public void setFirstButtonText(String firstButtonText) {
        this.firstButtonText = firstButtonText;
    }

    public String getFirstButtonUrl() {
        return firstButtonUrl;
    }

    public void setFirstButtonUrl(String firstButtonUrl) {
        this.firstButtonUrl = firstButtonUrl;
    }

    public String getSecondButtonText() {
        return secondButtonText;
    }

    public void setSecondButtonText(String secondButtonText) {
        this.secondButtonText = secondButtonText;
    }

    public String getSecondButtonUrl() {
        return secondButtonUrl;
    }

    public void setSecondButtonUrl(String secondButtonUrl) {
        this.secondButtonUrl = secondButtonUrl;
    }

    public List<Object> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Object> photos) {
        this.photos = photos;
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

    public Integer getSavedCount() {
        return savedCount;
    }

    public void setSavedCount(Integer savedCount) {
        this.savedCount = savedCount;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
