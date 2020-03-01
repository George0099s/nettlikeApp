package com.nettlike.app.view.followers.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nettlike.app.model.Follower;
import com.nettlike.app.view.followers.callback.CallbackFollower;
import com.nettlike.app.view.followers.data.FollowersRepository;

import java.util.ArrayList;
import java.util.List;

public class FollowersViewModel extends ViewModel implements CallbackFollower {
    private static final String TAG = "FollowersViewModel";

    private List<Follower> followers = new ArrayList<>();
    private String accountId, token;
    private int offset, limit;
    private MutableLiveData<List<Follower>> followerMutableLiveData;
    private FollowersRepository followersRepository;

    public FollowersViewModel(String accountId, String token, int offset, int limit, FollowersRepository followersRepository) {

        this.accountId = accountId;
        this.token = token;
        this.offset = offset;
        this.limit = limit;
        this.followersRepository = followersRepository;
        followerMutableLiveData = new MutableLiveData<>();
//        init();

    }

    private void init() {
//        if (followerMutableLiveData != null) {
//            return;
//        }
       followersRepository.getFollowersLiveData();
    }

    public List<Follower> getFollowers() {

        return followers;
    }

    public MutableLiveData<List<Follower>> getNewsRepository() {
        followersRepository.getFollowersLiveData();
        return followerMutableLiveData;
    }


    @Override
    public void setData(List<Follower> followerList) {

        followerMutableLiveData.setValue(followerList);
    }
}
