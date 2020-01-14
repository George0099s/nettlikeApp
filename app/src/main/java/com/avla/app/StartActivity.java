package com.avla.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.avla.app.Authorization.RegistrationActivity;

public class StartActivity extends AppCompatActivity {
    SharedPreferences mPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mPrefs = getSharedPreferences("com.avla.app", MODE_PRIVATE);
        startActivity(new Intent(StartActivity.this, OnBoarding.class));
        if (mPrefs.getBoolean("firstrun", true)) {

            mPrefs.edit().putBoolean("firstrun", false).commit();
        } else {
            startActivity(new Intent(StartActivity.this, RegistrationActivity.class));
        }
    }
}
