package com.avla.app.Model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PayloadModel {
    @NonNull
    @Override
    public String toString() {
        return ""+exists;
    }
    @SerializedName("exists")
    private boolean exists;
    @SerializedName("_id")
    private String id;
    @SerializedName("id")
    private String iD;
    @SerializedName("ok")
    private Boolean ok;
    @SerializedName("account_id")
    private String accountId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("status")
    private String status;
    @SerializedName("token")
    private String token;
    @SerializedName("is_registered")
    private String is_registered;
    @SerializedName("name")
    private String name;
    @SerializedName("population")
    private Integer population;
    @SerializedName("payloadList")
    private List<PayloadModel> payloadList = null;




    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIs_registered() {
        return is_registered;
    }

    public void setIs_registered(String is_registered) {
        this.is_registered = is_registered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public List<PayloadModel> getPayloadList() {
        return payloadList;
    }
}
