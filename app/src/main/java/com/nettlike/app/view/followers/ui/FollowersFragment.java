package com.nettlike.app.view.followers.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowersFragment extends Fragment {

    private static final String TAG = "FollowersFragment";
    NettlikeApplication nettlikeApplication;
    private FollowersFollowingAdapter followersFollowingAdapter;
    private RecyclerView followersRecycler;
    private String token, accountId;
    Observer<List<Follower>> userListUpdateObserver = new Observer<List<Follower>>() {
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
        followersRepository = new FollowersRepository(accountId, offset, limit, token);
        followersRecycler = view.findViewById(R.id.followers_recycler);
        followersRecycler.setHasFixedSize(true);
        followersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        followersViewModel = ViewModelProviders.of(getActivity(), new FollowersViewModelFactory(followersRepository)).get(FollowersViewModel.class);
        followersViewModel.getNewsRepository().observe(getActivity(), userListUpdateObserver);


        followersRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
                int visibleItemCount = linearLayout.getChildCount();
                int totalItemCount = linearLayout.getItemCount();
                int firstVisibleItemPosition = linearLayout.findFirstVisibleItemPosition();
//                    if ((visibleItemCount + firstVisibleItemPosition) < totalItemCount && firstVisibleItemPosition > 0) {
//
//                        followersRepository.setOffset(25);
//                    }
                if (dy % 10 != 0) {
                    followersRepository.setOffset(25);
                    followersViewModel.getNewsRepository().observe(getActivity(), userListUpdateObserver);
                }
            }
        });
    }

    @Override
    public void onStop() {
        followersViewModel.getNewsRepository().removeObserver(userListUpdateObserver);
        super.onStop();

    }

    @Override
    public void onStart() {
        super.onStart();
        followersViewModel.getNewsRepository().observe(getActivity(), userListUpdateObserver);
    }
}
