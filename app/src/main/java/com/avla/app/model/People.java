package com.avla.app.model;

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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDescription() {
        return description;
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
}
