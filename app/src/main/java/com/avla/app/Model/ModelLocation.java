package com.avla.app.Model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelLocation {


    @SerializedName("ok")
    private Boolean ok;
    @SerializedName("payload")
    private List<Payload> payload = null;


    @SerializedName("ok")
    public Boolean getOk() {
        return ok;
    }

    @SerializedName("ok")
    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    @SerializedName("payload")
    public List<Payload> getPayload() {
        return payload;
    }

    @SerializedName("payload")
    public void setPayload(List<Payload> payload) {
        this.payload = payload;
    }

    @NonNull
    @Override
    public String toString() {
        return ok + " " + payload;
    }
}
