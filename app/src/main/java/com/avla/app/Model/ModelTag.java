package com.avla.app.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelTag {

    @SerializedName("ok")
    private Boolean ok;
    @SerializedName("payload")
    private List<PayloadTag> payload = null;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public List<PayloadTag> getPayload() {
        return payload;
    }

    public void setPayload(List<PayloadTag> payload) {
        this.payload = payload;
    }
}
