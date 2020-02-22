package com.nettlike.app.view.main.profileInnerFragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nettlike.app.Constants;
import com.nettlike.app.Interface.IServer;
import com.avla.app.R;
import com.nettlike.app.adapter.PostAdapter;
import com.nettlike.app.adapter.SwipeToDeleteCallback;
import com.nettlike.app.model.Post;
import com.nettlike.app.model.UserModel;
import com.nettlike.app.model.UserPayload;
import com.nettlike.app.model.UserSingleton;

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
public class PostsInnerFragment extends Fragment {

    private static final String TAG = "PostsInnerFragment";
    private RecyclerView postsRecyclerView;
    private PostAdapter postAdapter;
    private String token;
    private UserSingleton userSingleton = UserSingleton.INSTANCE;
    private LinearLayoutManager linearLayoutManager;

    public PostsInnerFragment() {
        
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_inner, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        token = getActivity().getIntent().getStringExtra("token");
        postsRecyclerView = view.findViewById(R.id.user_posts);
        postsRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        postsRecyclerView.setLayoutManager(linearLayoutManager);
        enableSwipeToDeleteAndUndo();
        Observable.just((getUserPost()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private Boolean getUserPost() {
        String token = getActivity().getIntent().getStringExtra("token");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<UserModel> call = service.getUserInfo(token);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel object = response.body();
                UserPayload userPayload = object.getPayload();
                postAdapter = new PostAdapter(userPayload.getPosts(), token, getContext());
                postsRecyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });

        return true;
    }
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Post item = postAdapter.getData().get(position);
                    postAdapter.removeItem(position, item.getPost().getId());


            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(postsRecyclerView);
    }

}
