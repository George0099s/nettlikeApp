package com.nettlike.app.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmailValidate {

    @NonNull
    @Override
    public String toString() {
        return ok+" "+ payload;
    }
    @SerializedName("ok")
    private boolean ok;
    @SerializedName("error")
    private String error;
    @SerializedName("payload")
    private PayloadModel payload;
    @SerializedName("account_id")
    private String accountId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("status")
    private String status;
    @SerializedName("token")
    private String token;
    @SerializedName("payload")
    private List<PayloadModel> payloadList = null;

    public PayloadModel getPayload() {
        return payload;
    }

    public void setPayload(PayloadModel payload) {
        this.payload = payload;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<PayloadModel> getPayloadList() {
        return payloadList;
    }

    public void setPayloadList(List<PayloadModel> payloadList) {
        this.payloadList = payloadList;
    }
}
