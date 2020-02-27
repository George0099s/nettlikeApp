package com.nettlike.app.profile.data;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.nettlike.app.Constants;
import com.nettlike.app.data.AppDatabase;
import com.nettlike.app.data.TokenDao;
import com.nettlike.app.model.PayloadTag;
import com.nettlike.app.model.UserModel;
import com.nettlike.app.model.UserPayload;
import com.nettlike.app.model.UserSingleton;
import com.nettlike.app.profile.network.UserInfoAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileRepository {

    private MutableLiveData<UserSingleton> followerLiveData;
    private String token;
    private UserSingleton user;
    private AppDatabase db;
    private TokenDao tokenDao;

    public ProfileRepository(Context context) {
        db = Room.databaseBuilder(context,
                AppDatabase.class, "avla_DB")
                .allowMainThreadQueries()
                .build();
        tokenDao = db.tokenDao();
        token = tokenDao.getAll().get(0).getToken();
        db.close();
        user = UserSingleton.INSTANCE;
    }

    public MutableLiveData<UserSingleton> getProfileLiveData() {
        MutableLiveData<UserSingleton> userLiveData = new MutableLiveData<>();
        getUserInfo(new Callback<UserModel>() {
            @Override
            public void onResponse(retrofit2.Call<UserModel> call, Response<UserModel> response) {
                if (response.body() != null) {
                    UserModel model = response.body();
                    UserPayload userPayload = model.getPayload();
                    user.setUserId(userPayload.getId());
                    user.setJobTitle(userPayload.getJobTitle());
                    user.setCountry(userPayload.getCountry());
                    user.setCity(userPayload.getCity());
                    user.setFirstName(userPayload.getFirstName());
                    user.setLastName(userPayload.getLastName());
                    user.setAboutMyself(userPayload.getDesctiption());
                    user.setAge(userPayload.getAge());
                    user.setEvents(userPayload.getEvents());
                    user.setSavedPosts(userPayload.getSavedPosts());
                    user.setPictureURL(userPayload.getPictureUrl());
                    user.setSavedPeople(userPayload.getSavedPeople());
                    user.setCount_publication(userPayload.getPosts().size() + userPayload.getEvents().size());
                    user.setFollowers((ArrayList<String>) userPayload.getFollowers());
                    user.setFollowing((ArrayList<String>) userPayload.getFollowing());
                    ArrayList<String> tags = new ArrayList<>();
                    List<PayloadTag> payloadTags = userPayload.getTags();
                    for (int i = 0; i < payloadTags.size(); i++) {
                        tags.add(payloadTags.get(i).getName());
                    }
                    user.setEmail(userPayload.getEmail());
                    user.setTagsName(tags);
                    userLiveData.setValue(user);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<UserModel> call, Throwable t) {
                Log.d("LOG", "Something went wrong :c");
            }
        });
        return userLiveData;
    }

    public void getUserInfo(Callback<UserModel> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        UserInfoAPI service = retrofit.create(UserInfoAPI.class);
        Call<UserModel> call = service.getUserInfo(token);
        call.enqueue(callback);
    }
}
