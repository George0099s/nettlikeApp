package com.nettlike.app.profile.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.nettlike.app.profile.data.ProfileRepository;
import com.nettlike.app.profile.viewmodel.ProfileViewModel;

public class ProfileViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String token;
    private ProfileRepository profileRepository;

    public ProfileViewModelFactory(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == ProfileViewModel.class) {
            return (T) new ProfileViewModel(profileRepository);

        }
        return null;
    }
}
