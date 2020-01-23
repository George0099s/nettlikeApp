package com.avla.app.model;

import com.google.gson.annotations.SerializedName;

import javax.inject.Inject;

import dagger.Module;

@Module
public class User  {

    private String firstName;
    private String lastName;
    private String age;
    @SerializedName("ok")
    private Boolean ok;
    @SerializedName("payload")
    private UserPayload payload;

    public Boolean getOk() {
        return ok;
    }

    @Inject
    public User(){

    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public UserPayload getPayload() {
        return payload;
    }

    public void setPayload(UserPayload payload) {
        this.payload = payload;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
