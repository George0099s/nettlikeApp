package com.avla.app.fragments.Main.ProfileInnerFragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.model.User;
import com.avla.app.model.UserPayload;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileInnerFragment extends Fragment {

    private static final String TAG = "ProfileInnerFragment";
    private static final Scheduler THREAD_2 = Schedulers.newThread();
    private ImageView sendEmal;
    private TextView userJob, aboutUser;
    GetUserInfo getUserInfo;
    public ProfileInnerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        Observable.just((getUserInfo()))
                .subscribeOn(Schedulers.io())
                .observeOn(THREAD_2)
                .subscribe();
//        getUserInfo.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_inner, container, false);
        initViews(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void initViews(View view) {
        userJob = view.findViewById(R.id.user_job);
        aboutUser = view.findViewById(R.id.aboutUser);
        sendEmal = view.findViewById(R.id.send_email);
        sendEmal.setOnClickListener(this::onClick);

//        getUserInfo = new GetUserInfo();
//        getUserInfo.execute();
        Observable.just((getUserInfo()))
                .subscribeOn(Schedulers.io())
                .observeOn(THREAD_2)
                .subscribe();
    }

    @Override
    public void onResume() {
        super.onResume();
        Observable.just((getUserInfo()))
                .subscribeOn(Schedulers.io())
                .observeOn(THREAD_2)
                .subscribe();
    }
    private boolean getUserInfo() {
        String token = getActivity().getIntent().getStringExtra("token");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<User> call = service.getUserInfo(token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                User object = response.body();
                UserPayload userPayload = object.getPayload();
                Log.d(TAG, "onResponse: innerFragmentProfile  " + userPayload.getJobTitle() + "  " + userPayload.getDesctiption());
                userJob.setText(userPayload.getJobTitle());
                aboutUser.setText(userPayload.getDesctiption());
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });
        return true;
    }

    private void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }


    private class GetUserInfo extends AsyncTask<String, Void, Void> {
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        String token = getActivity().getIntent().getStringExtra("token");
        @Override
        protected Void doInBackground(String... strings) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASIC_URL) // Адрес сервера
                    .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                    .build();

            IServer service = retrofit.create(IServer.class);
            Call<User> call = service.getUserInfo(token);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    User object = response.body();
                    UserPayload userPayload = object.getPayload();
                    Log.d(TAG, "onResponse: innerFragmentProfile  " + userPayload.getJobTitle() + "  " + userPayload.getDesctiption());
                    userJob.setText(userPayload.getJobTitle());
                    aboutUser.setText(userPayload.getDesctiption());
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
                }
            });

            return null;
        }
    }
}
