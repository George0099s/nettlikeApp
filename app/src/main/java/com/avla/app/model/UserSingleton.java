package com.avla.app.model;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class UserSingleton {
    private int count_publication;
    private String firstName;
    private String lastName;
    private String age;
    private String aboutMyself;
    private String country;
    private String city;
    private String pictureId;
    private String email;
    private String userId;
    private String jobTitle;
    private String token;
    private ArrayList<String> tagsName = new ArrayList<>();
    private Boolean isExist = false;
    private ArrayList<String> followers = new ArrayList<>();
    private ArrayList<String> savedPeople = new ArrayList<>();
    private ArrayList<String> following = new ArrayList<>();
    private List<Event> events = new ArrayList<>();
    private List<Event> savedEvents = new ArrayList<>();
    private ArrayList<String> savedPosts;
    private List<Post> posts = new ArrayList<>();
    private JSONArray tagList = new JSONArray();

    public static final UserSingleton INSTANCE = new UserSingleton();

    private UserSingleton(){}



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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAboutMyself() {
        return aboutMyself;
    }

    public void setAboutMyself(String aboutMyself) {
        this.aboutMyself = aboutMyself;
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

    public JSONArray getTagList() {
        return tagList;
    }

    public void setTagList(JSONArray tagList) {
        this.tagList = tagList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getExist() {
        return isExist;
    }

    public void setExist(Boolean exist) {
        isExist = exist;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void clearAll(){
        this.firstName = null;
        this.lastName = null;
        this.age = null;
        this.jobTitle = null;
        this.aboutMyself = null;
        this.country = null;
        this.city = null;
        this.pictureId = null;
        this.email = null;
        this.userId = null;
        this.jobTitle = null;
        this.token = null;
        this.isExist =false;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<String> following) {
        this.following = following;
    }

    public ArrayList<String> getSavedPeople() {
        return savedPeople;
    }

    public void setSavedPeople(ArrayList<String> savedPeople) {
        this.savedPeople = savedPeople;
    }

    public ArrayList<String> getTagsName() {
        return tagsName;
    }

    public void setTagsName(ArrayList<String> tagsName) {
        this.tagsName = tagsName;
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

    public List<Event> getSavedEvents() {
        return savedEvents;
    }

    public void setSavedEvents(List<Event> savedEvents) {
        this.savedEvents = savedEvents;
    }

    public ArrayList<String> getSavedPosts() {
        return savedPosts;
    }

    public void setSavedPosts(ArrayList<String> savedPosts) {
        this.savedPosts = savedPosts;
    }

    public int getCount_publication() {
        return count_publication;
    }

    public void setCount_publication(int count_publication) {
        this.count_publication = count_publication;
    }
}
