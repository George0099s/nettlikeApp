package com.avla.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageMember {

    @SerializedName("_id")
    private String id;
    @SerializedName("status")
    private String status;
    @SerializedName("is_admin")
    private Boolean isAdmin;
    @SerializedName("invite_code")
    private String inviteCode;
    @SerializedName("activation_code")
    private String activationCode;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("apple_device_token")
    private String appleDeviceToken;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("patronymic_name")
    private String patronymicName;
    @SerializedName("description")
    private String description;
    @SerializedName("birth_year")
    private String birthYear;
    @SerializedName("country")
    private String country;
    @SerializedName("city")
    private String city;
    @SerializedName("job_title")
    private String jobTitle;

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

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
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

    public List<String> getSavedQuestions() {
        return savedQuestions;
    }

    public void setSavedQuestions(List<String> savedQuestions) {
        this.savedQuestions = savedQuestions;
    }

    public List<String> getSavedPeople() {
        return savedPeople;
    }

    public void setSavedPeople(List<String> savedPeople) {
        this.savedPeople = savedPeople;
    }

    public List<String> getSavedEvents() {
        return savedEvents;
    }

    public void setSavedEvents(List<String> savedEvents) {
        this.savedEvents = savedEvents;
    }

    public List<Object> getHiddenQuestions() {
        return hiddenQuestions;
    }

    public void setHiddenQuestions(List<Object> hiddenQuestions) {
        this.hiddenQuestions = hiddenQuestions;
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

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
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

    public String getPdfId() {
        return pdfId;
    }

    public void setPdfId(String pdfId) {
        this.pdfId = pdfId;
    }

    public String getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(String pdfFile) {
        this.pdfFile = pdfFile;
    }

    //    @SerializedName("mapTags")
//    private List<Tag> mapTags = null;
    @SerializedName("saved_questions")
    private List<String> savedQuestions = null;
    @SerializedName("saved_people")
    private List<String> savedPeople = null;
    @SerializedName("saved_events")
    private List<String> savedEvents = null;
    @SerializedName("hidden_questions")
    private List<Object> hiddenQuestions = null;
    @SerializedName("facebook_url")
    private String facebookUrl;
    @SerializedName("twitter_url")
    private String twitterUrl;
    @SerializedName("github_url")
    private String githubUrl;
    @SerializedName("lang")
    private String lang;
    @SerializedName("gender")
    private String gender;
    @SerializedName("picture_id")
    private String pictureId;
    @SerializedName("pdf_title")
    private String pdfTitle;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("following")
    private List<String> following = null;
    @SerializedName("followers")
    private List<String> followers = null;
    @SerializedName("apple_id")
    private String appleId;
    @SerializedName("pdf_id")
    private String pdfId;
    @SerializedName("pdf_file")
    private String pdfFile;
}
