package com.avla.app.model;

import androidx.annotation.NonNull;

public class Token {
    @NonNull
    @Override
    public String toString() {
        return token +""+ payload+""+ok+""+error;
    }

    private String token;
    private Payload payload;
    private Boolean ok;
    private String error;
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
