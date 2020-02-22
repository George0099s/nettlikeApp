package com.nettlike.app.view.followers;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nettlike.app.Constants;
import com.nettlike.app.Interface.IServer;
import com.avla.app.R;
import com.nettlike.app.adapter.FollowersFollowingAdapter;
import com.nettlike.app.model.Follower;
import com.nettlike.app.model.FollowerModel;
import com.nettlike.app.model.UserSingleton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowersFragment extends Fragment {

    private static final String TAG = "FollowersFragment";
    private FollowersFollowingAdapter followersFollowingAdapter;
    private RecyclerView followersRecycler;
    private String token, accountId;
    private UserSingleton user = UserSingleton.INSTANCE;
    private ArrayList<String> tags = new ArrayList<>();
    private int offset = 0;
    private int limit = 25;

    public FollowersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_followers, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        token = getActivity().getIntent().getStringExtra("token");
        accountId = getActivity().getIntent().getStringExtra("account_id");

        followersRecycler = view.findViewById(R.id.followers_recycler);
        followersRecycler.setHasFixedSize(true);
        followersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        getFollowers();
    }

    private void getFollowers() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();
        IServer service = retrofit.create(IServer.class);
        Call<FollowerModel> call = service.getFollowers(accountId, offset, limit, token);
        call.enqueue(new Callback<FollowerModel>() {
            @Override
            public void onResponse(Call<FollowerModel> call, Response<FollowerModel> response) {
                Log.d("AAA", "onResponse: " + call.request().url());
                FollowerModel model = response.body();
                List<Follower> followers = model.getPayload();

                followersFollowingAdapter = new FollowersFollowingAdapter(followers, token, getContext());
                followersRecycler.setAdapter(followersFollowingAdapter);
            }
            @Override
            public void onFailure(Call<FollowerModel> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });
    }

}
