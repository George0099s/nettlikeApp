package com.avla.app.fragments.main.peopleInner;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.model.Account;
import com.avla.app.model.Model;
import com.avla.app.model.Payload;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnotherUserProfileFrament extends Fragment {
    private static final String TAG = "AnotherUserProfileFrame";
    private TextView userJob, aboutUser;
    private String token, userId;

    public AnotherUserProfileFrament() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_another_user_profile_frament, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        userJob = view.findViewById(R.id.job_title);
        aboutUser = view.findViewById(R.id.about_user);
        token = getActivity().getIntent().getStringExtra("token");
        userId = getActivity().getIntent().getStringExtra("user id");
        rxRequest();
    }

    private void rxRequest(){
        Observable.just((getUserInfo(userId, token)))
                .subscribeOn(Schedulers.io())
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
                userJob.setText(account.getJobTitle());
                aboutUser.setText(account.getAboutYourself());
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return true;
    }
}
