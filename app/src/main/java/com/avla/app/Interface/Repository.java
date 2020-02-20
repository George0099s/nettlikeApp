package com.avla.app.Interface;

public class Repository implements SearchContract.Repository{
    private static final String TAG = "Repository";
    private String s;

    public Repository(String s) {
        this.s = s;
    }

    @Override
    public String loadMessage() {


        return this.s;
    }
}
