package com.nettlike.app.posts.postInner;

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
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nettlike.app.Constants;
import com.nettlike.app.Interface.IServer;
import com.nettlike.app.R;
import com.nettlike.app.adapter.ViewPagerAdapter;
import com.nettlike.app.data.AppDatabase;
import com.nettlike.app.data.TokenDao;
import com.nettlike.app.model.ModelPost;
import com.nettlike.app.model.PayloadTag;
import com.nettlike.app.model.Post;
import com.nettlike.app.model.PostPayload;
import com.nettlike.app.posts.PostAdapter;
import com.nettlike.app.posts.PostFragment;
import com.nettlike.app.posts.data.AllPostRepository;
import com.nettlike.app.posts.viewmodel.AllPostViewModel;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

public class AllPostFragment extends Fragment {
    private static final String TAG = "PopularPostEvent";
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String token;
    private JSONArray tags = new JSONArray();
    private List<PayloadTag> tagList = new ArrayList<>();
    private List<PayloadTag> lastTagList = new ArrayList<>();
    private String category = "popular_all_time";
    private int offset = 0;
    private int limit = 25;
    private String query = "";
    private RecyclerView postRecycler;
    private PostAdapter postAdapter;
    private List<Post> posts;
    private boolean isLoading = false;
    private Context context;
    boolean f1 = false;
    private ConstraintLayout constraintLayout;
    private AppDatabase db;
    private TokenDao tokenDao;
    private MutableLiveData<List<Post>> allPostLivedata;
    private AllPostViewModel allPostViewModel;
    private AllPostRepository allPostRepository;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_posts, container, false);
        initViews(view);

        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
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
            int visibleThreshold = totalItemCount - visibleItemCount;
//            if (dy % 10 != 0){
//                Log.d(TAG, "onScrolled: ");
//                AllPostRepository allPostRepository2 = new AllPostRepository(context,allPostLivedata, tags, category,query, offset+=25, limit);
//                allPostViewModel = ViewModelProviders.of(getActivity(), new AllPostViewModelFactory(allPostRepository2)).get(AllPostViewModel.class);
//                allPostViewModel.setData();
//                allPostViewModel.getAllPostMutableLiveData().observe(getActivity(), allPostListObserver);
//                tagList = PostFragment.getTags();
//            }
            int offset2 = 25;

            if (isLoading)
                if (dy % 10 != 0) {
                    if (totalItemCount <= (firstVisibleItemPosition + visibleItemCount))
                        if (posts.size() != 0) {
                            Observable.fromCallable((new CallableGetPost(offset2 += 25, query)))
                                    .subscribeOn(Schedulers.io())
                                    .subscribe();
                            isLoading = false;
                        }
                }

//                    allPostRepository = new AllPostRepository(context,allPostLivedata, tags, category,query, offset+=25, limit);
//                    allPostViewModel = ViewModelProviders.of(getActivity(), new AllPostViewModelFactory(allPostRepository)).get(AllPostViewModel.class);
//                    allPostViewModel.init();
//                    allPostViewModel.getProfileMutableLiveData().observe(getActivity(), allPostListObserver);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        Observable.fromCallable((new CallableGetPost(offset, query)))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
//
//    private Observer<List<Post>> allPostListObserver = postList -> {
//        if (postAdapter == null) {
//            postAdapter = new PostAdapter(postList, token, getContext());
//            postRecycler.setAdapter(postAdapter);
//            Log.d(TAG, "123: ");
//        } else {
//            postAdapter.addAll(postList);
//            Log.d(TAG, ":321 ");
//        }
//    };

    private void initViews(View view) {
        context = getContext();
//        postAdapter = new PostAdapter();
        db = Room.databaseBuilder(context,
                AppDatabase.class, "avla_DB")
                .allowMainThreadQueries()
                .build();
//        allPostRepository = new AllPostRepository(context,allPostLivedata, tags, category,query, offset, limit);
//        allPostViewModel = ViewModelProviders.of(getActivity(), new AllPostViewModelFactory(allPostRepository)).get(AllPostViewModel.class);
//        allPostViewModel.getAllPostMutableLiveData().observe(getActivity(), allPostListObserver);

        postRecycler = view.findViewById(R.id.post_recycler);
        constraintLayout = view.findViewById(R.id.l);
        postRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        postRecycler.setHasFixedSize(true);
        postRecycler.addOnScrollListener(scrollListener);
//        postRecycler.setAdapter(postAdapter);
        tagList = PostFragment.getTags();
    }

    @Override
    public void onStop() {
//        allPostViewModel.getAllPostMutableLiveData().removeObserver(allPostListObserver);
        super.onStop();
    }

    private Boolean getPopularPost(int offset, String query) {
        tokenDao = db.tokenDao();
        token = tokenDao.getAll().get(0).getToken();
        db.close();
        tagList = PostFragment.getTags();
        tags = new JSONArray();
        for (int i = 0; i <tagList.size() ; i++) {
            tags.put(tagList.get(i).getId());
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<ModelPost> call = service.getPosts(token, tags,category, offset, limit, query);
        Log.d(TAG, "all: " + call.request().url());
        call.enqueue(new Callback<ModelPost>() {

            @Override
            public void onResponse(Call<ModelPost> call, Response<ModelPost> response) {

                ModelPost modelPost = response.body();
                PostPayload postPayload = modelPost.getPayload();
//                if (modelPost.getPayload() != null)
                posts = postPayload.getPosts();
                if (tagList.size() == 0) {
                    if (posts.size() != 0) {
                        if (postAdapter == null) {
                            postAdapter = new PostAdapter(posts, token, getContext());

                            postRecycler.setAdapter(postAdapter);
                            postAdapter.notifyDataSetChanged();
                            isLoading = true;
                        } else {
                            postAdapter.addAll(posts);
                            isLoading = true;
                        }
                    } else {
//                            postAdapter.deleteAll();
                        postAdapter = null;
                        Toast.makeText(getContext(), "There are no posts", Toast.LENGTH_SHORT).show();
                        isLoading = false;
                    }
                }
                if (tagList.size() != 0) {
                    if (posts.size() != 0) {
                        if (!f1) {
                            postAdapter = new PostAdapter(posts, token, getContext());
                            postRecycler.setAdapter(postAdapter);
                            isLoading = true;
                            lastTagList = tagList;
                            f1 = true;
                        } else {
                            if (tagList == lastTagList) {
                                postAdapter.addAll(posts);
                                isLoading = true;
                            } else {
                                postAdapter = new PostAdapter(posts, token, getContext());
                                postRecycler.setAdapter(postAdapter);
                                isLoading = true;
                                f1 = false;
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "There are no posts", Toast.LENGTH_SHORT).show();
                        postAdapter = null;
                        f1 = false;
                        isLoading = false;
                    }
                } else {
                    postAdapter = null;
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

    private class CallableGetPost implements Callable<Boolean> {
        int offset;

        public CallableGetPost(int offset, String query) {
            this.offset = offset;
            this.query = query;
        }

        String query;
        @Override
        public Boolean call() throws Exception {
            return getPopularPost(offset, query);
        }
    }
}
