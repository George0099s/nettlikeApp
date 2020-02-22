package com.nettlike.app.view.main.peopleInner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.nettlike.app.Constants;
import com.nettlike.app.Interface.IServer;
import com.nettlike.app.R;
import com.nettlike.app.adapter.ViewPagerAdapter;
import com.nettlike.app.model.Account;
import com.nettlike.app.model.Model;
import com.nettlike.app.model.Payload;
import com.nettlike.app.model.Token;
import com.nettlike.app.model.UserSingleton;
import com.nettlike.app.model.dialog.DiaMember;
import com.nettlike.app.model.dialog.DiaPayload;
import com.nettlike.app.model.dialog.DialogModel;
import com.nettlike.app.view.DialogActivity;
import com.nettlike.app.view.followers.FollowersActivity;

import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AnotherUserProfileActivity extends AppCompatActivity {

    private static final String TAG = "AnotherUserProfile";
    TextView userName, userLocation, followers, following, publication;
    UserSingleton userSingleton = UserSingleton.INSTANCE;
    private String token, userId, accountId;
    private ViewPager viewPager;
    private ImageView userImg;
    private Button follow;
    private TabLayout tabLayout;
    private View followersSector, followinSector;
    private ProgressDialog pd;
    private ViewPagerAdapter viewPagerAdapter;

    //    String userId, pictureId, diaogId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        if (savedInstanceState == null) {
            initViews();
        }


    }

    private void initViews() {

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());

        followersSector = findViewById(R.id.another_user_followers_sector);
        followersSector.setOnClickListener(this::onclick);

        followinSector = findViewById(R.id.another_user_following_sector);
        followinSector.setOnClickListener(this::onclick);
        follow = findViewById(R.id.folow_btn);
        userImg = findViewById(R.id.user_image);
        publication = findViewById(R.id.publications_text_view);
        followers = findViewById(R.id.followers_another_user);
        following = findViewById(R.id.following_another_user);
        token = getIntent().getStringExtra("token");
        userId = getIntent().getStringExtra("user id");
        userName = findViewById(R.id.follower_username);
        userLocation = findViewById(R.id.user_location);

        if (userId.equals(userSingleton.getUserId()))
            follow.setVisibility(View.GONE);
        rxRequest(userId, token);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.profile_viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new AnotherUserProfileFrament(), "Profile");
        viewPagerAdapter.addFragment(new AnotherUserPostsFragment(), "Posts");
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        findViewById(R.id.folow_btn).setOnClickListener(this::onclick);
    }

    private void onclick(View view) {
        switch (view.getId()) {

            case R.id.folow_btn:
                Observable.fromCallable((new CallableStartStopFollow(userId)))
                        .subscribeOn(Schedulers.io())
                        .subscribe();
                rxRequest(userId, token);
                break;
            case R.id.another_user_followers_sector:
                Intent intent2 = new Intent(this, FollowersActivity.class);
                intent2.putExtra("token", token);
                intent2.putExtra("type", "followers");
                intent2.putExtra("account_id", userId);
                startActivity(intent2);
                break;
            case R.id.another_user_following_sector:
                Intent intent3 = new Intent(this, FollowersActivity.class);
                intent3.putExtra("token", token);
                intent3.putExtra("type", "following");
                intent3.putExtra("account_id", userId);
                startActivity(intent3);
                break;
        }
    }

    private Boolean startNewDia() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();
        IServer service = retrofit.create(IServer.class);
        Call<DialogModel> call = service.startDia(userId, token);

        call.enqueue(new Callback<DialogModel>() {

            @Override
            public void onResponse(Call<DialogModel> call, Response<DialogModel> response) {
                DialogModel diaModel = response.body();
                if (diaModel.getOk()) {
                    DiaPayload payload = diaModel.getPayload();
                    String dialogId = payload.getId();
                    List<DiaMember> members = payload.getMembers();
                    String pictureId = null;
                    String id = null;
                    for (DiaMember member : members) {
                        if (!member.getId().equals(userSingleton.getUserId())) {
                            pictureId = member.getPictureId();
                            id = member.getId();
                        }
                    }
                    Intent intent = new Intent(AnotherUserProfileActivity.this, DialogActivity.class);
                    intent.putExtra("user id", id);
                    intent.putExtra("picture id", pictureId);
                    intent.putExtra("dialog id", dialogId);
                    intent.putExtra("token", token);
                    startActivity(intent);
                } else {
                    Toast.makeText(AnotherUserProfileActivity.this, "Something went wrong, pls try later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DialogModel> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });
        return null;
    }

    private Boolean startStopFollow(String userId) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();
        IServer service = retrofit.create(IServer.class);
        Call<Token> call = service.followUnfollow(userId, token);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Log.d(TAG, "onResponse: start following");

            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });
        return true;
    }

    private void rxRequest(String userId, String token) {
        pd = new ProgressDialog(this);
        pd.setMessage("loading");
        pd.show();
        Observable.fromCallable(new CallableGetUserInfo(userId, token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        pd.cancel();
    }

    private Boolean getUserInfo(String id, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<Model> call = service.getAnotherUserInfo(id, token);
        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                Model model = response.body();
                Payload payload = model.getPayload();
                Account account = payload.getAccount();
                accountId = payload.getId();
                userName.setText(String.format("%s %s", account.getFirstName(), account.getLastName()));
                userLocation.setText(String.format("%s, %s", account.getCountry(), account.getCity()));
                Glide.with(AnotherUserProfileActivity.this).load(account.getPictureUrl()).apply(RequestOptions.circleCropTransform()).into(userImg);
                followers.setText(String.valueOf(account.getFollowers().size()));
                following.setText(String.valueOf(account.getFollowing().size()));
                publication.setText(String.valueOf(account.getPosts().size() + account.getEvents().size()));
                for (int i = 0; i < account.getFollowers().size(); i++) {
                    if (account.getFollowers().get(i).equals(userSingleton.getUserId())) {
                        follow.setText(R.string.unfollow_string);
                    } else {
                        follow.setText(R.string.followbtn_string);
                    }
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return true;
    }

    private class CallableGetUserInfo implements Callable {
        private String userId;
        private String token;

        private CallableGetUserInfo(String userId, String token) {
            this.userId = userId;
            this.token = token;
        }

        @Override
        public Object call() throws Exception {
            return getUserInfo(userId, token);
        }
    }
    private class CallableStartStopFollow implements Callable<Boolean>{
        public CallableStartStopFollow(String userId) {
            this.userId = userId;
        }

        String userId;
        @Override
        public Boolean call() throws Exception {
            return startStopFollow(userId);
        }
    }
    private class CallableStartNewDia implements Callable {


        private CallableStartNewDia() {

        }

        @Override
        public Object call() throws Exception {
            return startNewDia();
        }
    }

}
