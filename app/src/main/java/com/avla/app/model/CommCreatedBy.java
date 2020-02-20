package com.avla.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommCreatedBy {

    @SerializedName("picture_url")
    @Expose
    private String pictureUrl;

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
