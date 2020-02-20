package com.avla.app.view.main.postInner;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.Interface.SearchContract;
import com.avla.app.Interface.SearchInterface;
import com.avla.app.R;
import com.avla.app.adapter.PostAdapter;
import com.avla.app.model.ModelPost;
import com.avla.app.model.Post;
import com.avla.app.model.PostPayload;
import com.avla.app.view.main.PostFragment;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
public class FeedPostFragment extends Fragment implements SearchContract.View {
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
    private SearchInterface searchInterface;
    public FeedPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_post, container, false);
        // Inflate the layout for this fragment
        searchInterface = (SearchInterface) getFragmentManager().findFragmentByTag("4");
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        token = Objects.requireNonNull(getActivity()).getIntent().getStringExtra("token");

        postRecycler = view.findViewById(R.id.all_post_recycler);
        postRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        postRecycler.setHasFixedSize(true);
        postRecycler.addOnScrollListener(scrollListener);

        Observable.just((getPopularPost(offset, searchInterface.getQuery())))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    @Override
    public void onResume() {
        super.onResume();
        Observable.just((getPopularPost(0, searchInterface.getQuery())))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

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
                        getPopularPost(offset += 25 , searchInterface.getQuery());
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
                if (searchInterface.getQuery().equals("") || searchInterface.getQuery().isEmpty())
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
                    if(posts.size() != 0) {
                            postAdapter = new PostAdapter(posts, token, getContext());
                            postRecycler.setAdapter(postAdapter);
                            isLoading = true;
                    } else{
                        isLoading = false;
                    }
                }else{
                    postAdapter = new PostAdapter(posts, token, getContext());
                    postRecycler.setAdapter(postAdapter);
                    isLoading = true;
//                    f1 = true;
                }

            }

            @Override
            public void onFailure(Call<ModelPost> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.printStackTrace();
            }
        });

        return true;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
    @Override
    public void search(String query) {

    }
}
