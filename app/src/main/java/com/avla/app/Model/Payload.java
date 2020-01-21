package com.avla.app.Model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Payload {

    @NonNull
    @Override
    public String toString() {
        return id+" "+name+" "+population + "" +ok;
    }
    @SerializedName("exists")
    private Boolean exists;
    @SerializedName("id")
    private String id;
    @SerializedName("token")
    private String token;
    @SerializedName("name")
    private String name;
    @SerializedName("population")
    private Integer population;
    @SerializedName("ok")
    private Boolean ok;
    @SerializedName("id")
    public String getId() {
        return id;
    }

    @SerializedName("id")
    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("name")
    public String getName() {
        return name;
    }

    @SerializedName("name")
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("population")
    public Integer getPopulation() {
        return population;
    }

    @SerializedName("population")
    public void setPopulation(Integer population) {
        this.population = population;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public Boolean getExists() {
        return exists;
    }

    public void setExists(Boolean exists) {
        this.exists = exists;
    }
}
