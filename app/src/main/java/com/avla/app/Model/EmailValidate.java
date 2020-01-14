package com.avla.app.Model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EmailValidate {

    @NonNull
    @Override
    public String toString() {
        return ok+" "+ payload;
    }

    @SerializedName("ok")
    private boolean ok;
    @SerializedName("payload")
    private PayloadModel payload;

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


//    private class Payload {
//        @NonNull
//        @Override
//        public String toString() {
//            return ""+exists;
//        }
//
//        @SerializedName("exists")
//        private boolean exists;
//
//        public boolean isExists() {
//            return exists;
//        }
//
//        public void setExists(boolean exists) {
//            this.exists = exists;
//        }
//    }
}
