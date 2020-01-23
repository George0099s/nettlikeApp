package com.avla.app.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class EmailPojo {
    @SerializedName("ok")
    private Boolean ok;
    @SerializedName("payload")
    private Payload payload;
    @SerializedName("error")
    private String error;

    @NonNull
    @Override
    public String toString() {
        return ok + " " + payload + " " + error;
    }

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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
