package com.avla.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModelTag {
    public static List<PayloadTag> tagsPayload;
    public static HashMap<String, String> mapTags = new HashMap<>();
    public static ArrayList<String> ParenIds = new ArrayList<>();

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
