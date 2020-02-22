package com.nettlike.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelPost {
    @SerializedName("payload")
    private PostPayload payload;
    @SerializedName("ok")
    @Expose
    private Boolean ok;

    public PostPayload getPayload() {
        return payload;
    }

    public void setPayload(PostPayload payload) {
        this.payload = payload;
    }

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }
}
