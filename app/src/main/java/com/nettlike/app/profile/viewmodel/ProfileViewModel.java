package com.nettlike.app.profile.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nettlike.app.model.UserSingleton;
import com.nettlike.app.profile.data.ProfileRepository;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<UserSingleton> profileMutableLiveData;
    private ProfileRepository profileRepository;

    public ProfileViewModel(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
        init();
    }

    public void setData() {
        profileMutableLiveData.postValue(UserSingleton.INSTANCE);
    }

    private void init() {
//        if (profileMutableLiveData != null) {
//            return;
//        }
        profileMutableLiveData = profileRepository.getProfileLiveData();
    }


    public MutableLiveData<UserSingleton> getProfileMutableLiveData() {
        return profileMutableLiveData;
    }


}
