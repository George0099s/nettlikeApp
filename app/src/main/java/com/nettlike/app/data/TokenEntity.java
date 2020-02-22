package com.nettlike.app.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TokenEntity {
    public TokenEntity() { }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "token")
    private String token;

    @NonNull
    @Override
    public String toString() {
        return token+"";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
