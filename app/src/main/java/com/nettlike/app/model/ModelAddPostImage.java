package com.nettlike.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelAddPostImage {
    @SerializedName("payload")
    private Boolean payload;
    @SerializedName("ok")
    @Expose
    private Boolean ok;

    public Boolean getPayload() {
        return payload;
    }

    public void setPayload(Boolean payload) {
        this.payload = payload;
    }

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }
}
