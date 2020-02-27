package com.nettlike.app.profile.network;

import com.nettlike.app.model.UserModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserInfoAPI {
    @GET("public_api/account/info")
    Call<UserModel> getUserInfo(@Query("token") String token);
}
