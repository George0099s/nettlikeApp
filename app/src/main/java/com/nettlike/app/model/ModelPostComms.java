package com.nettlike.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelPostComms {
    @SerializedName("payload")
    private PostComsPayload payload;
    @SerializedName("ok")
    @Expose
    private Boolean ok;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public PostComsPayload getPayload() {
        return payload;
    }

    public void setPayload(PostComsPayload payload) {
        this.payload = payload;
    }
}
