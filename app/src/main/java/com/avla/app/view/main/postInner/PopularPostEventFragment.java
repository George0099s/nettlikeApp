package com.avla.app.view.main.postInner;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.Interface.SearchInterface;
import com.avla.app.R;
import com.avla.app.adapter.PostAdapter;
import com.avla.app.adapter.ViewPagerAdapter;
import com.avla.app.model.ModelPost;
import com.avla.app.model.Post;
import com.avla.app.model.PostPayload;
import com.avla.app.view.main.PostFragment;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PopularPostEventFragment extends Fragment {
    private static final String TAG = "PopularPostEvent";
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String token;
    private JSONArray tags = new JSONArray();
    private ArrayList<String> tagList = new ArrayList<>();
    private String category = "popular_all_time";
    private int offset = 0;
    private int limit = 25;
    private String query = "";
    private RecyclerView postRecycler;
    private PostAdapter postAdapter;
    private List<Post> posts;
    private boolean isLoading = false;
    private Context context;
    private SearchInterface searchInterface;
    boolean f1 = false;
    private ConstraintLayout constraintLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular_post_event, container, false);
        searchInterface = (SearchInterface) getFragmentManager().findFragmentByTag("4");
        initViews(view);

        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        Toast.makeText(context, searchInterface.getQuery(), Toast.LENGTH_SHORT).show();
//        Observable.just((getPopularPost(0, searchInterface.getQuery())))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe();
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.d(TAG, "onResume: "+ searchInterface.getQuery());

        Toast.makeText(context, searchInterface.getQuery(), Toast.LENGTH_SHORT).show();
        Observable.just((getPopularPost(0, searchInterface.getQuery())))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
    public void getData(){
        Observable.just((getPopularPost(0, searchInterface.getQuery())))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
    private void initViews(View view) {
        token = getActivity().getIntent().getStringExtra("token");
        context = getContext();
        postRecycler = view.findViewById(R.id.post_recycler);
        constraintLayout = view.findViewById(R.id.l);
                postRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        postRecycler.setHasFixedSize(true);
        postRecycler.addOnScrollListener(scrollListener);
        Observable.just((getPopularPost(offset, searchInterface.getQuery())))
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
            if (dx < 0) {
                Observable.just((getPopularPost(0, searchInterface.getQuery())))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            }
                if (isLoading)
                if (dy % 10 != 0 && dy > 0) {
                    if (posts.size() != 0) {
                        getPopularPost(offset += 25, searchInterface.getQuery());
                        isLoading = false;
                    }
                } else if(dy < 0){
                    getPopularPost(0, searchInterface.getQuery());
                }
        }
    };

    private boolean getPopularPost(int offset, String query) {

        tagList = PostFragment.getTags();
         tags = new JSONArray();
        for (int i = 0; i <tagList.size() ; i++) {
            tags.put(tagList.get(i));
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<ModelPost> call = service.getPosts(token, tags,category, offset, limit, query);
        Log.d(TAG, "getPopularPost: " + call.request().url());
        call.enqueue(new Callback<ModelPost>() {
            @Override
            public void onResponse(Call<ModelPost> call, Response<ModelPost> response) {

                ModelPost modelPost = response.body();
                PostPayload postPayload = modelPost.getPayload();
                posts = postPayload.getPosts();
                if (searchInterface.getQuery().equals("") || searchInterface.getQuery().length() < 1){
                    if (tagList.size() == 0)
                        if (posts.size() != 0) {
                            if (postAdapter == null) {
                                postAdapter = new PostAdapter(posts, token, getContext());
                                postRecycler.setAdapter(postAdapter);
                                isLoading = true;
                            } else {
                                postAdapter.addAll(posts);
                                isLoading = true;
                            }
                        } else {
                            postAdapter = null;
                            isLoading = false;
                        }
                    } else {
                        if (!f1) {
                            postAdapter = new PostAdapter(posts, token, getContext());
                            postRecycler.setAdapter(postAdapter);
                            isLoading = true;
                            f1 = true;
                        } else {
                            postAdapter.addAll(posts);
                            isLoading = true;
                        }
                    }
                    if (searchInterface.getQuery().equals("") || searchInterface.getQuery().length() < 1) {
                        if (tagList.size() != 0) {
                            if (posts.size() != 0) {
                                if (!f1) {
                                    postAdapter = new PostAdapter(posts, token, getContext());
                                    postRecycler.setAdapter(postAdapter);
                                    isLoading = true;
                                    f1 = true;
                                } else {
                                    postAdapter.addAll(posts);
                                    isLoading = true;
                                }
                            } else {
                                postAdapter = null;
                                isLoading = false;
                            }
                        }
                    }else {
                            if (!f1) {
                                postAdapter = new PostAdapter(posts, token, getContext());
                                postRecycler.setAdapter(postAdapter);
                                isLoading = true;
                                f1 = true;
                            } else {
                                postAdapter.addAll(posts);
                                isLoading = true;
                            }
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
}
