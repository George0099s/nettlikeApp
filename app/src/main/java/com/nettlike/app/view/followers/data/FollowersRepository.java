package com.nettlike.app.view.followers.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.nettlike.app.Constants;
import com.nettlike.app.model.Follower;
import com.nettlike.app.model.FollowerModel;
import com.nettlike.app.view.followers.callback.CallbackFollower;
import com.nettlike.app.view.followers.network.FollowersApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FollowersRepository {
    private static final String TAG = "FollowersRepository";
    private List<Follower> followerList = new ArrayList<>();
    private String accountId, token;
    private int offset, limit;
    private MutableLiveData<List<Follower>> followerLiveData;
    private CallbackFollower callback;

    public FollowersRepository(String accountId, int offset, int limit, String token) {
        this.accountId = accountId;
        this.token = token;
        this.limit = limit;
    }

    public void registerCallBack(CallbackFollower callback){
        this.callback = callback;
    }

//    public MutableLiveData<List<Follower>> getFollowersLiveData() {
//        followerLiveData = new MutableLiveData<>();
public void getFollowersLiveData() {

        getFollowerList(new Callback<FollowerModel>() {
            @Override
            public void onResponse(retrofit2.Call<FollowerModel> call, Response<FollowerModel> response) {
                if (response.body() != null) {

                    FollowerModel model = response.body();
                    followerList = model.getPayload();
                    callback.setData(followerList);


//                    followerLiveData.setValue(followerList);

                }
            }

            @Override
            public void onFailure(retrofit2.Call<FollowerModel> call, Throwable t) {
                Log.d("LOG", "Something went wrong :c");
            }
        });
//        return followerLiveData;
    }

    private void getFollowerList(Callback<FollowerModel> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        FollowersApi service = retrofit.create(FollowersApi.class);
        Call<FollowerModel> call = service.getFollowers(accountId, offset, limit, token);
        call.enqueue(callback);
    }

    public void setOffset(int offset) {
        this.offset += offset;
    }
}
