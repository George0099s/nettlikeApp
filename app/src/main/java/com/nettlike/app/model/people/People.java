package com.nettlike.app.model.people;

import com.google.gson.annotations.SerializedName;
import com.nettlike.app.model.PayloadTag;

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
    @SerializedName("country")
    private String country;
    @SerializedName("city")
    private String city;
    @SerializedName("tags")
    private List<PayloadTag> tag;
    @SerializedName("picture_url")
    private String pictureUrl;
    @SerializedName("job_title")
    private String jobTitle;

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

    public List<PayloadTag> getTag() {
        return tag;
    }

    public void setTag(List<PayloadTag> tag) {
        this.tag = tag;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
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
}
