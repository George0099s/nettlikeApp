package com.nettlike.app.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class UserEntity {
    @ColumnInfo(name = "first name")
    private String firstName;
    @ColumnInfo(name = "last name")
    private String lastName;
    @ColumnInfo(name = "age")
    private String age;
    @ColumnInfo(name = "about myself")
    private String aboutMyself;
    @ColumnInfo(name = "country")
    private String country;
    @ColumnInfo(name = "city")
    private String city;
    @ColumnInfo(name = "pictureId")
    private String pictureId;
    @ColumnInfo(name = "email")
    private String email;
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String userId;
    @ColumnInfo(name = "job title")
    private String jobTitle;
    @ColumnInfo(name = "is exist")
    private Boolean isExist = false;
//    @ColumnInfo(name = "mapTags")
//    private JSONArray tagList = new JSONArray();

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Boolean getExist() {
        return isExist;
    }

    public void setExist(Boolean exist) {
        isExist = exist;
    }

//    public JSONArray getTagList() {
//        return tagList;
//    }
//
//    public void setTagList(JSONArray tagList) {
//        this.tagList = tagList;
//    }
}
