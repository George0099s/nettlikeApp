package com.avla.app.view.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.avla.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddContentFragment extends Fragment {
    private CardView createEvent, createPost;
    String token;
    public AddContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_content, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        createEvent = view.findViewById(R.id.card_create_event);
        createPost = view.findViewById(R.id.card_create_post);
        createEvent.setOnClickListener(this::onclick);
        createPost.setOnClickListener(this::onclick);
        token = getActivity().getIntent().getStringExtra("token");
    }

    private void onclick(View view) {
        switch (view.getId()){
            case R.id.card_create_event:
                Intent intent2 = new Intent(getContext(), CreateEventActivity.class);
                intent2.putExtra("token", token);
                startActivity(intent2);
                break;
            case R.id.card_create_post:
                Intent intent = new Intent(getContext(), CreatePostActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
                break;


        }
    }

}
