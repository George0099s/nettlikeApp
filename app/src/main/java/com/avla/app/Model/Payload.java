package com.avla.app.Model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Payload {

    @NonNull
    @Override
    public String toString() {
        return id+" "+name+" "+population;
    }

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("population")
    private Integer population;

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
}
