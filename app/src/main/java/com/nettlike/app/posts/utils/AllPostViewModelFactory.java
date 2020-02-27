package com.nettlike.app.posts.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.nettlike.app.posts.data.AllPostRepository;
import com.nettlike.app.posts.viewmodel.AllPostViewModel;

import org.json.JSONArray;

public class AllPostViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    JSONArray tags;
    String category, query;
    int offset, limit;
    private String token;
    private AllPostRepository allPostRepository;


    public AllPostViewModelFactory(String token, AllPostRepository allPostRepository, JSONArray tags, String category, String query, int offset, int limit) {
        this.token = token;
        this.allPostRepository = allPostRepository;
        this.tags = tags;
        this.category = category;
        this.query = query;
        this.offset = offset;
        this.limit = limit;
    }

    public AllPostViewModelFactory(AllPostRepository allPostRepository) {
        this.allPostRepository = allPostRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == AllPostViewModel.class) {
            return (T) new AllPostViewModel(allPostRepository);
        }
        return null;
    }
}
