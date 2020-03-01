package com.nettlike.app.view.followers.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nettlike.app.NettlikeApplication;
import com.nettlike.app.R;
import com.nettlike.app.adapter.FollowersFollowingAdapter;
import com.nettlike.app.model.Follower;
import com.nettlike.app.model.UserSingleton;
import com.nettlike.app.view.followers.data.FollowersRepository;
import com.nettlike.app.view.followers.utils.FollowersViewModelFactory;
import com.nettlike.app.view.followers.viewmodel.FollowersViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowersFragment extends Fragment {

    private static final String TAG = "FollowersFragment";
    NettlikeApplication nettlikeApplication;
    private FollowersFollowingAdapter followersFollowingAdapter;
    private RecyclerView followersRecycler;
    private String token, accountId;
    private Observer<List<Follower>> userListUpdateObserver = new Observer<List<Follower>>() {
        @Override
        public void onChanged(List<Follower> followers) {
            if (followersFollowingAdapter == null) {
                followersFollowingAdapter = new FollowersFollowingAdapter(followers, token, getContext());
            } else {
                followersFollowingAdapter.addAll(followers);
            }
            followersRecycler.setAdapter(followersFollowingAdapter);
        }

    };
    private UserSingleton user = UserSingleton.INSTANCE;
    private ArrayList<String> tags = new ArrayList<>();
    private int offset = 0;
    private int limit = 100;
    private FollowersViewModel followersViewModel;
    private FollowersRepository followersRepository;
    private boolean isLoading = false;


    public FollowersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {

        token = getActivity().getIntent().getStringExtra("token");
        accountId = getActivity().getIntent().getStringExtra("account_id");
        followersRecycler = view.findViewById(R.id.followers_recycler);
        followersRecycler.setHasFixedSize(true);
        followersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));


        followersRepository = new FollowersRepository(accountId, offset, limit, token);
        followersViewModel = new ViewModelProvider(getActivity(), new FollowersViewModelFactory(followersRepository)).get(FollowersViewModel.class);
        followersRepository.registerCallBack(followersViewModel);
        followersViewModel.getNewsRepository().observe(getActivity(), userListUpdateObserver);


        followersRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
                int visibleItemCount = linearLayout.getChildCount();
                int totalItemCount = linearLayout.getItemCount();
                int firstVisibleItemPosition = linearLayout.findFirstVisibleItemPosition();
                if (isLoading)
                    if (totalItemCount <= (firstVisibleItemPosition + visibleItemCount)){
                    followersRepository.setOffset(25);
                    followersViewModel.getNewsRepository().observe(Objects.requireNonNull(getActivity()), userListUpdateObserver);
                }
            }
        });
    }
}
