package com.avla.app.Model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class PayloadModel {
    @NonNull
    @Override
    public String toString() {
        return ""+exists;
    }

    @SerializedName("exists")
    private boolean exists;

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
}
