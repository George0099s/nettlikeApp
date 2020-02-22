package com.nettlike.app.view.main.peopleInner;


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
import com.nettlike.app.adapter.PostAdapter;
import com.nettlike.app.model.Account;
import com.nettlike.app.model.Model;
import com.nettlike.app.model.Payload;
import com.nettlike.app.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnotherUserPostsFragment extends Fragment {
    private static final String TAG = "AnotherUserPostsFragmen";
    private RecyclerView postsRecyclerView;
    private PostAdapter postAdapter;
    private String token, currentUserId;

    public AnotherUserPostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_another_user_post, container, false);
        initView(view);
        return view;
    }

private void initView(View view) {

    token = getActivity().getIntent().getStringExtra("token");
    currentUserId = getActivity().getIntent().getStringExtra("user id");
    postsRecyclerView = view.findViewById(R.id.another_user_post);
    postsRecyclerView.setHasFixedSize(true);
    postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    Observable.just((getUserPost()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe();

    }

    private boolean getUserPost() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<Model> call = service.getAnotherUserInfo(currentUserId,token);
        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                Model model= response.body();
                Payload payload = model.getPayload();
                Account account = payload.getAccount();
                List<Post> posts = account.getPosts();

                postAdapter = new PostAdapter(posts, token, getContext());
                postsRecyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return true;
    }
}
