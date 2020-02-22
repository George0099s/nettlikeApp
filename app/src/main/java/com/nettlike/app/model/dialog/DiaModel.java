package com.nettlike.app.model.dialog;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DiaModel {

    @SerializedName("payload")
    private List<DiaPayload> payloadList = new ArrayList<>();
    public List<DiaPayload> getPayloadList() {
        return payloadList;
    }

    public void setPayloadList(List<DiaPayload> payloadList) {
        this.payloadList = payloadList;
    }

}
