package com.nettlike.app.view.followers.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nettlike.app.model.Follower;
import com.nettlike.app.view.followers.data.FollowingRepository;

import java.util.ArrayList;
import java.util.List;

public class FollowingViewModel extends ViewModel {
    private static final String TAG = "FollowingViewModel";
    private List<Follower> following = new ArrayList<>();
    private String accountId, token;
    private int offset, limit;
    private MutableLiveData<List<Follower>> followingMutableLiveData;
    private FollowingRepository followingRepository;

    public FollowingViewModel(String accountId, String token, int offset, int limit, FollowingRepository followingRepository) {

        this.accountId = accountId;
        this.token = token;
        this.offset = offset;
        this.limit = limit;
        this.followingRepository = followingRepository;
        init();

    }

    private void init() {
        if (followingMutableLiveData != null) {
            return;
        }
        followingMutableLiveData = followingRepository.getFollowingLiveData();
    }

    public List<Follower> getFollowers() {
        return following;
    }

    public MutableLiveData<List<Follower>> getFollowingMutableLiveData() {
        return followingMutableLiveData;
    }
}
