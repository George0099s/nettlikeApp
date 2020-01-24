package com.avla.app.model;

import org.json.JSONArray;

public class UserSingleton {
    private String firstName;
    private String lastName;
    private String age;
    private String aboutMyself;
    private String country;
    private String city;
    private String jobTitle;
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
}
