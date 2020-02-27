package com.nettlike.app.posts.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nettlike.app.model.Post;
import com.nettlike.app.posts.data.AllPostRepository;

import java.util.List;

public class AllPostViewModel extends ViewModel {
    private MutableLiveData<List<Post>> allPostMutableLiveData;
    private AllPostRepository allPostRepository;

    public AllPostViewModel(AllPostRepository allPostRepository) {
        this.allPostRepository = allPostRepository;
        init();
    }


    private void init() {
        if (allPostMutableLiveData != null) {
            return;
        }
        allPostMutableLiveData = allPostRepository.getAllPostLiveData();
    }

    public void setData() {
        this.allPostMutableLiveData = allPostRepository.getAllPostLiveData();
    }

    public MutableLiveData<List<Post>> getAllPostMutableLiveData() {
        return allPostMutableLiveData;
    }
}
