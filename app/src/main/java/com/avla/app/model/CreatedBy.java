package com.avla.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreatedBy {
    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("is_admin")
    @Expose
    private Boolean isAdmin;
    @SerializedName("invite_code")
    @Expose
    private String inviteCode;
    @SerializedName("apple_device_token")
    @Expose
    private String appleDeviceToken;
    @SerializedName("tags")
    @Expose
    private List<Object> tags = null;
    @SerializedName("saved_posts")
    @Expose
    private List<Object> savedPosts = null;
    @SerializedName("saved_people")
    @Expose
    private List<Object> savedPeople = null;
    @SerializedName("saved_events")
    @Expose
    private List<Object> savedEvents = null;
    @SerializedName("hidden_posts")
    @Expose
    private List<Object> hiddenPosts = null;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("pdf_title")
    @Expose
    private String pdfTitle;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("following")
    @Expose
    private List<Object> following = null;
    @SerializedName("followers")
    @Expose
    private List<Object> followers = null;
    @SerializedName("picture_url")
    @Expose
    private String pictureUrl;


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

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getAppleDeviceToken() {
        return appleDeviceToken;
    }

    public void setAppleDeviceToken(String appleDeviceToken) {
        this.appleDeviceToken = appleDeviceToken;
    }

    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    public List<Object> getSavedPosts() {
        return savedPosts;
    }

    public void setSavedPosts(List<Object> savedPosts) {
        this.savedPosts = savedPosts;
    }

    public List<Object> getSavedPeople() {
        return savedPeople;
    }

    public void setSavedPeople(List<Object> savedPeople) {
        this.savedPeople = savedPeople;
    }

    public List<Object> getSavedEvents() {
        return savedEvents;
    }

    public void setSavedEvents(List<Object> savedEvents) {
        this.savedEvents = savedEvents;
    }

    public List<Object> getHiddenPosts() {
        return hiddenPosts;
    }

    public void setHiddenPosts(List<Object> hiddenPosts) {
        this.hiddenPosts = hiddenPosts;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPdfTitle() {
        return pdfTitle;
    }

    public void setPdfTitle(String pdfTitle) {
        this.pdfTitle = pdfTitle;
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

    public List<Object> getFollowing() {
        return following;
    }

    public void setFollowing(List<Object> following) {
        this.following = following;
    }

    public List<Object> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Object> followers) {
        this.followers = followers;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
