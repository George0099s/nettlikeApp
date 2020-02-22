package com.nettlike.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MessagePayload {

    @SerializedName("dialog")
    @Expose
    private MessageDialog messageDialog;
    @SerializedName("messages")
    @Expose
    private List<Message> messages = new ArrayList<>();

    public MessagePayload() {
    }

    public MessageDialog getMessageDialog() {
        return messageDialog;
    }

    public void setMessageDialog(MessageDialog messageDialog) {
        this.messageDialog = messageDialog;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
