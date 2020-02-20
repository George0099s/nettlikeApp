package com.avla.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelEvent {
    @SerializedName("ok")
    @Expose
    private Boolean ok;
    @SerializedName("payload")
    @Expose
    private EventPayload payload;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public EventPayload getPayload() {
        return payload;
    }

    public void setPayload(EventPayload payload) {
        this.payload = payload;
    }
}
