package com.avla.app.model;


import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class  MessageModel {
    @SerializedName("payload")
    @Expose
    private MessagePayload messagePayload;
    @SerializedName("ok")
    @Expose
    private Boolean ok;

    public MessageModel() {

    }

    @NonNull
    @Override
    public String toString() {
        return ok + " " + messagePayload  ;
    }

    public MessagePayload getMessagePayload() {
        return messagePayload;
    }

    public void setMessagePayload(MessagePayload messagePayload) {
        this.messagePayload = messagePayload;
    }

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

}
