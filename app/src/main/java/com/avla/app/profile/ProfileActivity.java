package com.avla.app.profile;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.avla.app.Interface.IServer;
import com.avla.app.model.User;
import com.avla.app.model.UserPayload;
import com.avla.app.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private String token;
    TextView userName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
    }

    private void initViews() {
        token = getIntent().getStringExtra("token");
        Log.d(TAG, "initViews: " + token);
        getData(token);
        userName = findViewById(R.id.job_title);
    }


    private void getData(String token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.getavla.com/") // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<User> call = service.getUserInfo(token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                User object = response.body();
                UserPayload userPayload = object.getPayload();
                Log.d(TAG, "onResponse: name" + userPayload.getFirstName());
                userName.setText(userPayload.getFirstName() + " " + userPayload.getLastName());

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });
    }
}
