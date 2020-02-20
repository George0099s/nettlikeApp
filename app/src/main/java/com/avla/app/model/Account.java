package com.avla.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Account {
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("country")
    private String country;
    @SerializedName("city")
    private String city;
    @SerializedName("picture_url")
    private String pictureUrl;
    @SerializedName("job_title")
    private String jobTitle;
    @SerializedName("description")
    private String aboutYourself;
    @SerializedName("followers")
    private List<String> followers = null;
    @SerializedName("following")
    private List<String> following = null;
    @SerializedName("tags")
    private List<PayloadTag> tags = null;
    @SerializedName("posts")
    private List<Post> posts = null;
    @SerializedName("comments")
    private List<Comment> comments = null;

    @SerializedName("events")
    private List<Event> events = null;

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


    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getAboutYourself() {
        return aboutYourself;
    }

    public void setAboutYourself(String aboutYourself) {
        this.aboutYourself = aboutYourself;
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

    public List<PayloadTag> getTags() {
        return tags;
    }

    public void setTags(List<PayloadTag> tags) {
        this.tags = tags;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
