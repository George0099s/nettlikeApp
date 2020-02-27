package com.nettlike.app.view.followers.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.nettlike.app.view.followers.data.FollowersRepository;
import com.nettlike.app.view.followers.data.FollowingRepository;
import com.nettlike.app.view.followers.viewmodel.FollowersViewModel;
import com.nettlike.app.view.followers.viewmodel.FollowingViewModel;

public class FollowersViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String accountId, token;
    private int offset, limit;
    private FollowersRepository followersRepository;
    private FollowingRepository followingRepository;

    public FollowersViewModelFactory(String accountId, String token, int offset, int limit, FollowersRepository followersRepository) {
        this.accountId = accountId;
        this.token = token;
        this.offset = offset;
        this.limit = limit;
        this.followersRepository = followersRepository;
    }

    public FollowersViewModelFactory(FollowersRepository followersRepository) {
        this.followersRepository = followersRepository;
    }

    public FollowersViewModelFactory(FollowingRepository followingRepository) {
        this.followingRepository = followingRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == FollowersViewModel.class) {
            return (T) new FollowersViewModel(accountId, token, offset, limit, followersRepository);
        } else if (modelClass == FollowingViewModel.class) {
            return (T) new FollowingViewModel(accountId, token, offset, limit, followingRepository);
        }
        return null;
    }
}
