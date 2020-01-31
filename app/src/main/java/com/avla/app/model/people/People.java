package com.avla.app.model.people;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class People {
    @SerializedName("_id")
    private String id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("description")
    private String description;
    @SerializedName("tags")
    private List tag;
    @SerializedName("picture_id")
    private String pictureId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName != null ? firstName : "No first name" ;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName != null ? lastName : "No last name" ;

    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescription() {
        return description != null ? description : "No description" ;

    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List getTag() {
        return tag;
    }

    public void setTag(List tag) {
        this.tag = tag;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }
}
