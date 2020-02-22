package com.nettlike.app.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserPayload {
    @SerializedName("account")
    private UserPayload payload;
    @SerializedName("country")
    private String country;
    @SerializedName("city")
    private String city;
    @SerializedName("_id")
    private String id;
    @SerializedName("birth_year")
    private String age;
    @SerializedName("description")
    private String desctiption;
    @SerializedName("activation_code")
    private String activationCode;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("email")
    private String email;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("followers")
    private List<String> followers = null;
    @SerializedName("following")
    private List<String> following = null;
    @SerializedName("gender")
    private String gender;
    @SerializedName("hidden_questions")
    private List<Object> hiddenQuestions = null;
    @SerializedName("invite_code")
    private String inviteCode;
    @SerializedName("is_admin")
    private Boolean isAdmin;
    @SerializedName("lang")
    private String lang;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("patronymic_name")
    private String patronymicName;
    @SerializedName("phone")
    private String phone;
    @SerializedName("saved_people")
    private ArrayList<String> savedPeople = null;
    @SerializedName("saved_posts")
    private ArrayList<String> savedPosts = null;
    @SerializedName("status")
    private String status;
    @SerializedName("tags")
    private List<PayloadTag> tags = null;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("job_title")
    private String jobTitle;
    @SerializedName("picture_url")
    private String pictureUrl;
    @SerializedName("events")
    private List<Event> events;
    @SerializedName("posts")
    private List<Post> posts;
    @SerializedName("comments")
    private List<Comment> comments;

    @NonNull
    @Override
    public String toString() {
        return tags +"";
    }

    public UserPayload() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Object> getHiddenQuestions() {
        return hiddenQuestions;
    }

    public void setHiddenQuestions(List<Object> hiddenQuestions) {
        this.hiddenQuestions = hiddenQuestions;
    }
    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<String> getSavedPeople() {
        return savedPeople;
    }

    public void setSavedPeople(ArrayList<String> savedPeople) {
        this.savedPeople = savedPeople;
    }

    public ArrayList<String> getSavedPosts() {
        return savedPosts;
    }

    public void setSavedPosts(ArrayList<String> savedPosts) {
        this.savedPosts = savedPosts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PayloadTag> getTags() {
        return tags;
    }

    public void setTags(List<PayloadTag> tags) {
        this.tags = tags;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDesctiption() {
        return desctiption;
    }

    public void setDesctiption(String desctiption) {
        this.desctiption = desctiption;
    }

    public String getAge() {
//        return String.valueOf(getCurrentYear() - Integer.parseInt(age));

            return age;
    }



    public void setAge(String age) {
        this.age = age;
    }
    public static int getCurrentYear()
    {
        java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getDefault(), java.util.Locale.getDefault());
        calendar.setTime(new java.util.Date());
        return calendar.get(java.util.Calendar.YEAR);
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

    public UserPayload getPayload() {
        return payload;
    }

    public void setPayload(UserPayload payload) {
        this.payload = payload;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}

