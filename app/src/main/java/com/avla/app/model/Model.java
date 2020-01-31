package com.avla.app.model;

import com.google.gson.annotations.SerializedName;

public class Model {
    @SerializedName("payload")
    private Payload payload;
    @SerializedName("ok")
    private Boolean ok;

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

}
