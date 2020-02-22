package com.nettlike.app.model.dialog;

import com.google.gson.annotations.SerializedName;

public class DiaMember {
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
    @SerializedName("picture_url")
    private String pictureId;

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

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }
}
