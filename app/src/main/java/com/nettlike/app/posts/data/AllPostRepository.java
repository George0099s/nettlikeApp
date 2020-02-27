package com.nettlike.app.posts.data;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.nettlike.app.Constants;
import com.nettlike.app.data.AppDatabase;
import com.nettlike.app.data.TokenDao;
import com.nettlike.app.model.ModelPost;
import com.nettlike.app.model.Post;
import com.nettlike.app.model.PostPayload;
import com.nettlike.app.posts.network.PostApi;

import org.json.JSONArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AllPostRepository {
    private static final String TAG = "AllPostRepository";
    private MutableLiveData<List<Post>> allPostLiveData;
    private String token;
    private AppDatabase db;
    private TokenDao tokenDao;
    private JSONArray tags;
    private String category;
    private String query;
    private int offset, limit;

    public AllPostRepository(Context context, MutableLiveData<List<Post>> allPostLiveData, JSONArray tags, String category, String query, int offset, int limit) {
        this.allPostLiveData = allPostLiveData;
        this.tags = tags;
        this.category = category;
        this.query = query;
        this.offset = offset;
        this.limit = limit;
        Log.d(TAG, "AllPostRepository: " + offset);
        db = Room.databaseBuilder(context,
                AppDatabase.class, "avla_DB")
                .allowMainThreadQueries()
                .build();
        tokenDao = db.tokenDao();
        token = tokenDao.getAll().get(0).getToken();
        db.close();
    }

    public MutableLiveData<List<Post>> getAllPostLiveData() {
        if (allPostLiveData == null)
            allPostLiveData = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        PostApi service = retrofit.create(PostApi.class);
        Call<ModelPost> call = service.getPosts(token, tags, category, offset, limit, query);
        call.enqueue(new Callback<ModelPost>() {
            @Override
            public void onResponse(retrofit2.Call<ModelPost> call, Response<ModelPost> response) {
                if (response.body() != null) {
                    ModelPost modelPost = response.body();
                    PostPayload postPayload = modelPost.getPayload();
                    List<Post> posts = postPayload.getPosts();
                    if (posts.size() > 0)
                        Log.d(TAG, "onResponse: " + posts.size());
                    allPostLiveData.setValue(posts);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ModelPost> call, Throwable t) {
                Log.d("LOG", "Something went wrong :c");
            }
        });
        return allPostLiveData;
    }
}
