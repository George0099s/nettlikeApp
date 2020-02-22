package com.nettlike.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Follower {
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
    @SerializedName("activation_code")
    @Expose
    private String activationCode;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("apple_device_token")
    @Expose
    private String appleDeviceToken;
    @SerializedName("android_registration_key")
    @Expose
    private String androidRegistrationKey;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("patronymic_name")
    @Expose
    private String patronymicName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("birth_year")
    @Expose
    private String birthYear;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("job_title")
    @Expose
    private String jobTitle;
    @SerializedName("saved_posts")
    @Expose
    private List<String> savedPosts = null;
    @SerializedName("saved_people")
    @Expose
    private List<Object> savedPeople = null;
    @SerializedName("saved_events")
    @Expose
    private List<String> savedEvents = null;
    @SerializedName("hidden_posts")
    @Expose
    private List<Object> hiddenPosts = null;
    @SerializedName("facebook_url")
    @Expose
    private String facebookUrl;
    @SerializedName("twitter_url")
    @Expose
    private String twitterUrl;
    @SerializedName("github_url")
    @Expose
    private String githubUrl;
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
    @SerializedName("picture_url")
    @Expose
    private String pictureUrl;
    @SerializedName("following")
    @Expose
    private List<String> following = null;
    @SerializedName("followers")
    @Expose
    private List<String> followers = null;
    @SerializedName("apple_id")
    @Expose
    private String appleId;

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

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAppleDeviceToken() {
        return appleDeviceToken;
    }

    public void setAppleDeviceToken(String appleDeviceToken) {
        this.appleDeviceToken = appleDeviceToken;
    }

    public String getAndroidRegistrationKey() {
        return androidRegistrationKey;
    }

    public void setAndroidRegistrationKey(String androidRegistrationKey) {
        this.androidRegistrationKey = androidRegistrationKey;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymicName() {
        return patronymicName;
    }

    public void setPatronymicName(String patronymicName) {
        this.patronymicName = patronymicName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public List<String> getSavedPosts() {
        return savedPosts;
    }

    public void setSavedPosts(List<String> savedPosts) {
        this.savedPosts = savedPosts;
    }

    public List<Object> getSavedPeople() {
        return savedPeople;
    }

    public void setSavedPeople(List<Object> savedPeople) {
        this.savedPeople = savedPeople;
    }

    public List<String> getSavedEvents() {
        return savedEvents;
    }

    public void setSavedEvents(List<String> savedEvents) {
        this.savedEvents = savedEvents;
    }

    public List<Object> getHiddenPosts() {
        return hiddenPosts;
    }

    public void setHiddenPosts(List<Object> hiddenPosts) {
        this.hiddenPosts = hiddenPosts;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
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

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public String getAppleId() {
        return appleId;
    }

    public void setAppleId(String appleId) {
        this.appleId = appleId;
    }
}
