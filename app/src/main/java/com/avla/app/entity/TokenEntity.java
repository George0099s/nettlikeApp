package com.avla.app.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TokenEntity {
    @PrimaryKey
    @NonNull
    private String token;

    @NonNull
    @Override
    public String toString() {
        return token+"";
    }

    public TokenEntity(@NonNull String token) {
        this.token = token;
    }

    public TokenEntity() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
