package com.avla.app.model.activity;

import com.avla.app.model.CreatedBy;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TargetEvent {
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
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("start_time")
    @Expose
    private String startTime;
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
    private List<String> photos = null;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("saved_count")
    @Expose
    private Integer savedCount;
    @SerializedName("created_by")
    @Expose
    private CreatedBy createdBy;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
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

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
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

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }
}
