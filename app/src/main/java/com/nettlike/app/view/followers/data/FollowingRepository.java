package com.nettlike.app.view.followers.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.nettlike.app.Constants;
import com.nettlike.app.model.Follower;
import com.nettlike.app.model.FollowerModel;
import com.nettlike.app.view.followers.network.FollowersApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FollowingRepository {
    private static final String TAG = "FollowingRepository";
    private List<Follower> followingList = new ArrayList<>();
    private String accountId, token;
    private int offset, limit;

    public FollowingRepository(String accountId, int offset, int limit, String token) {
        this.accountId = accountId;
        this.token = token;
        this.offset = offset;
        this.limit = limit;
    }

    public MutableLiveData<List<Follower>> getFollowingLiveData() {
        MutableLiveData<List<Follower>> followingLiveData = new MutableLiveData<>();
        getFollowingList(new Callback<FollowerModel>() {
            @Override
            public void onResponse(retrofit2.Call<FollowerModel> call, Response<FollowerModel> response) {
                if (response.body() != null) {
                    FollowerModel model = response.body();
                    followingList = model.getPayload();
                    followingLiveData.setValue(followingList);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<FollowerModel> call, Throwable t) {
                Log.d("LOG", "Something went wrong :c");
            }
        });
        return followingLiveData;
    }

    public void getFollowingList(Callback<FollowerModel> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        FollowersApi service = retrofit.create(FollowersApi.class);
        Call<FollowerModel> call = service.getFollowing(accountId, offset, limit, token);
        call.enqueue(callback);
    }
}
