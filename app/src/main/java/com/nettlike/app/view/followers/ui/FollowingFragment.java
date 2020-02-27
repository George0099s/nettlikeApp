package com.nettlike.app.view.followers.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nettlike.app.R;
import com.nettlike.app.adapter.FollowersFollowingAdapter;
import com.nettlike.app.model.Follower;
import com.nettlike.app.model.UserSingleton;
import com.nettlike.app.view.followers.data.FollowingRepository;
import com.nettlike.app.view.followers.utils.FollowersViewModelFactory;
import com.nettlike.app.view.followers.viewmodel.FollowingViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment {
    private static final String TAG = "FollowingFragment";
    private FollowersFollowingAdapter followersFollowingAdapter;
    private RecyclerView followersRecycler;
    private String token, accountId;
    Observer<List<Follower>> followingListUpdateObserver = new Observer<List<Follower>>() {
        @Override
        public void onChanged(List<Follower> followers) {
            followersFollowingAdapter = new FollowersFollowingAdapter(followers, token, getContext());
            followersRecycler.setAdapter(followersFollowingAdapter);
        }

    };
    private UserSingleton user = UserSingleton.INSTANCE;
    private ArrayList<String> tags = new ArrayList<>();
    private int offset = 0;
    private int limit = 100;
    private FollowingViewModel followersViewModel;
    private FollowingRepository followingRepository;

    public FollowingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_following, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        token = getActivity().getIntent().getStringExtra("token");
        accountId = getActivity().getIntent().getStringExtra("account_id");
        followingRepository = new FollowingRepository(accountId, offset, limit, token);
        followersRecycler = view.findViewById(R.id.following_recycler);
        followersRecycler.setHasFixedSize(true);
        followersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        followersViewModel = ViewModelProviders.of(getActivity(), new FollowersViewModelFactory(followingRepository)).get(FollowingViewModel.class);
        followersViewModel.getFollowingMutableLiveData().observe(getActivity(), followingListUpdateObserver);
    }
}
