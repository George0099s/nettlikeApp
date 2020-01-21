package com.avla.app.Model;

import com.google.gson.annotations.SerializedName;

public class User  {


    @SerializedName("ok")
    private Boolean ok;
    @SerializedName("payload")
    private UserPayload payload;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public UserPayload getPayload() {
        return payload;
    }

    public void setPayload(UserPayload payload) {
        this.payload = payload;
    }
}
