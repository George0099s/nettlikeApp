package com.nettlike.app.view.authorization.signUp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nettlike.app.R;
import com.nettlike.app.model.UserSingleton;
import com.nettlike.app.profile.UserTagAdapter;

import org.json.JSONArray;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpChooseTagsFragment extends Fragment {
    private static final String TAG = "SignUpChooseTagsFragmen";
    private SharedPreferences sharedPreferences;
    private ArrayList<String> tagListName;
    private ArrayList<String> tagListId = new ArrayList<>();
    private JSONArray tags = new JSONArray();
    private RecyclerView tagRecyclerView;
    private EditText jobTitle;
    private TextView chooseTags;
    private String token;
    private UserTagAdapter userTagAdapter;
    @Inject
    public SignUpChooseTagsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_sign_up_choose_tags, container, false);
        token = getActivity().getIntent().getStringExtra("token");


        chooseTags = view.findViewById(R.id.choose_tags);
        chooseTags.setOnClickListener(this::onClick);
        jobTitle = view.findViewById(R.id.follower_username);
        tagRecyclerView = view.findViewById(R.id.selected_tag);
        tagRecyclerView.setHasFixedSize(true);
        tagRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        jobTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                UserSingleton userSingleton = UserSingleton.INSTANCE;
                userSingleton.setJobTitle(jobTitle.getText().toString());
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        for (int i = 0; i < UserSingleton.INSTANCE.getTagsName().size(); i++) {
            tags.put(UserSingleton.INSTANCE.getTagsName().get(i));
        }

        userTagAdapter = new UserTagAdapter(tags, getContext());
        tagRecyclerView.setAdapter(userTagAdapter);
        Log.d(TAG, "onResume: " + tags);
    }

    private void onClick(View view) {
        Intent intent = new Intent(getContext(), SignUpChooseCategoryActivity.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }
}
