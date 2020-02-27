package com.nettlike.app.posts.network;

import com.nettlike.app.model.ModelPost;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PostApi {
    @GET("public_api/posts/discover")
    Call<ModelPost> getPosts(@Query("token") String token,
                             @Query("tags") JSONArray tags,
                             @Query("category") String category,
                             @Query("offset") int offset,
                             @Query("limit") int limit,
                             @Query("query") String query);
}
