package com.nettlike.app.view.followers.network;

import com.nettlike.app.model.FollowerModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FollowersApi {

    @GET("public_api/people/{account_id}/following")
    Call<FollowerModel> getFollowing(@Path("account_id") String accountId,
                                     @Query("offset") int offset,
                                     @Query("limit") int limit,
                                     @Query("token") String token);

    @GET("public_api/people/{account_id}/followers")
    Call<FollowerModel> getFollowers(@Path("account_id") String accountId,
                                     @Query("offset") int offset,
                                     @Query("limit") int limit,
                                     @Query("token") String token);
}
