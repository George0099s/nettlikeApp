package com.nettlike.app.model.people;

import com.google.gson.annotations.SerializedName;

public class PeopleModel {
    @SerializedName("ok")
    private Boolean ok;
    @SerializedName("payload")
    private PeoplePayload payload;



    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public PeoplePayload getPayload() {
        return payload;
    }

    public void setPayload(PeoplePayload payload) {
        this.payload = payload;
    }
}
