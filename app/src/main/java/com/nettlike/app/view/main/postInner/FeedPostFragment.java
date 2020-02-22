package com.nettlike.app.view.main.postInner;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nettlike.app.Constants;
import com.nettlike.app.Interface.IServer;
import com.avla.app.R;
import com.nettlike.app.adapter.PostAdapter;
import com.nettlike.app.model.ModelPost;
import com.nettlike.app.model.Post;
import com.nettlike.app.model.PostPayload;
import com.nettlike.app.view.main.PostFragment;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

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
public class FeedPostFragment extends Fragment {
    private static final String TAG = "FeedPostFragment";
    private String token;
    private ArrayList<String> tags = new ArrayList<>();
    private String category = "feed";
    private int offset = 0;
    private int limit = 25;
    private String query = "";
    private RecyclerView postRecycler;
    private PostAdapter postAdapter;
    private List<Post> posts;
    private boolean isLoading = false;
    public FeedPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_post, container, false);
        // Inflate the layout for this fragment
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        token = Objects.requireNonNull(getActivity()).getIntent().getStringExtra("token");
        postRecycler = view.findViewById(R.id.all_post_recycler);
        postRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        postRecycler.setHasFixedSize(true);
        postRecycler.addOnScrollListener(scrollListener);
        Observable.fromCallable(new CallableFeedPost(offset, query))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    @Override
    public void onResume() {
        super.onResume();
        tags = PostFragment.getTags();
//        Observable.fromCallable(new CallableFeedPost(offset, query))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe();
    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            int visibleItemCount = linearLayoutManager.getChildCount();
            int totalItemCount = linearLayoutManager.getItemCount();
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            if (isLoading)
                if (dy % 10 != 0) {
                    if (totalItemCount <= (firstVisibleItemPosition + visibleItemCount))
                    if (posts.size() != 0) {
                        Observable.fromCallable(new CallableFeedPost(offset += 25, query))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                        isLoading = false;
                    }
                }
        }
    };

    private boolean getPopularPost(int offset, String query) {
        tags = PostFragment.getTags();
        JSONArray j = new JSONArray();
        for (int i = 0; i <tags.size() ; i++) {
            j.put(tags.get(i));
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<ModelPost> call = service.getPosts(token, j,category, offset, limit, query);
        Log.d(TAG, "getPopularPost: " + call.request().url());
        call.enqueue(new Callback<ModelPost>() {
            @Override
            public void onResponse(Call<ModelPost> call, Response<ModelPost> response) {

                ModelPost modelPost = response.body();
                PostPayload postPayload = modelPost.getPayload();
                posts = postPayload.getPosts();
                    if (tags.size() == 0)
                if(posts.size() != 0) {
                    if (postAdapter == null) {
                        postAdapter = new PostAdapter(posts, token, getContext());
                        postRecycler.setAdapter(postAdapter);
                        isLoading = true;
                    } else {
                        postAdapter.addAll(posts);
                        isLoading = true;
                    }
                } else{
                    isLoading = false;
                }
                else {
                        if (posts.size() != 0) {
                            postAdapter = new PostAdapter(posts, token, getContext());
                            postRecycler.setAdapter(postAdapter);
                            isLoading = true;
                        } else {
                            isLoading = false;
                        }
                    }

            }

            @Override
            public void onFailure(retrofit2.Call<ModelPost> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.printStackTrace();
            }
        });

        return true;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    private class CallableFeedPost implements Callable<Boolean>{
        public CallableFeedPost(int offset, String query) {
            this.offset = offset;
            this.query = query;
        }

        int offset;
        String query;
        @Override
        public Boolean call() throws Exception {
            return getPopularPost(offset, query);
        }
    }
}
