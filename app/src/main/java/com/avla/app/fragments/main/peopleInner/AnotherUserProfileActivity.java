package com.avla.app.fragments.main.peopleInner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.adapter.ViewPagerAdapter;
import com.avla.app.fragments.DialogActivity;
import com.avla.app.model.Account;
import com.avla.app.model.Model;
import com.avla.app.model.Payload;
import com.avla.app.model.Token;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

public class AnotherUserProfileActivity extends AppCompatActivity {

    private static final String TAG = "AnotherUserProfile";
    private String token, userId;
    TextView userName, userLocation;
    private ViewPager viewPager;
    private ImageView userImg;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        if (savedInstanceState == null){
            initViews();
        }


    }

    private void initViews() {
        userImg = findViewById(R.id.user_image);
        token = getIntent().getStringExtra("token");
        userName = findViewById(R.id.job_title);
        userLocation = findViewById(R.id.user_location);
        userId = getIntent().getStringExtra("user id");
        rxRequest(userId,token);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.profile_viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new AnotherUserProfileFrament(),"Profile");
        viewPagerAdapter.addFragment(new AnotherUserQuestionFragment(),"Questions");
        viewPagerAdapter.addFragment(new AnotherUserAnswerFragment(),"Answers");
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        findViewById(R.id.write_btn).setOnClickListener(this::onclick);
        findViewById(R.id.folow_btn).setOnClickListener(this::onclick);
    }

    private void onclick(View view) {
        switch (view.getId()){
            case R.id.write_btn:
                Observable.just(startNewDia())
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe();
                Intent intent = new Intent(AnotherUserProfileActivity.this, DialogActivity.class);
                intent.putExtra("user id", userId);
                startActivity(intent);
            case R.id.folow_btn:
                startStopFollow(userId);
        }
    }

    private Boolean startNewDia() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();
        IServer service = retrofit.create(IServer.class);
        Call<Token> call = service.startDia(userId, token);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {

            }
            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });
        return null;
    }

    private void startStopFollow(String userId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();
        IServer service = retrofit.create(IServer.class);
        Call<Token> call = service.followUnfollow(userId, token);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Log.d(TAG, "onResponse: " + response.body() + "user id  " + userId);
            }
            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });
    }

    private void rxRequest(String userId,String token){
        Observable.just((getUserInfo(userId,token)))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
    }

    private Boolean getUserInfo(String id, String token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<Model> call = service.getAnotherUserInfo(id,token);
        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                Log.d(TAG, "onResponse: " + call.request().url());
                Model model= response.body();
                Payload payload = model.getPayload();
                Account account = payload.getAccount();
                userName.setText(String.format("%s %s", account.getFirstName(), account.getLastName()));
                userLocation.setText(String.format("%s, %s", account.getCountry(), account.getCity()));
                Glide.with(AnotherUserProfileActivity.this).load(Constants.BASIC_URL + "public_api/account/get_picture?picture_id="+account.getPictureId()).into(userImg);

            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return true;
    }


}
