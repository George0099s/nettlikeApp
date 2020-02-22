package com.nettlike.app.view.main.profileInnerFragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nettlike.app.R;
import com.nettlike.app.adapter.UserTagAdapter;
import com.nettlike.app.model.UserSingleton;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileInnerFragment extends Fragment {

    private static final String TAG = "ProfileInnerFragment";
    private ImageView sendEmal;
    private TextView userJob, aboutUser;
    private RecyclerView tagRecycler;
    private UserTagAdapter userTagAdapter;
    private JSONArray tagsJson;
    private UserSingleton user = UserSingleton.INSTANCE;
    public ProfileInnerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_inner, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initViews(View view) {

        tagRecycler = view.findViewById(R.id.tag_recycler);
        tagRecycler.setHasFixedSize(true);
        tagRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        userJob = view.findViewById(R.id.user_job);
        aboutUser = view.findViewById(R.id.aboutUser);
        sendEmal = view.findViewById(R.id.send_email);
        sendEmal.setOnClickListener(this::onClick);
        getUserInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
    }

    private boolean getUserInfo() {
        JSONArray tagsJsonArray = new JSONArray();
        ArrayList<String> tags = user.getTagsName();
        for (int i = 0; i < tags.size(); i++) {
            tagsJsonArray.put(tags.get(i));
        }

        userJob.setText(user.getJobTitle());
        aboutUser.setText(user.getAboutMyself());
        userTagAdapter = new UserTagAdapter(tagsJsonArray, getContext());
        tagRecycler.setAdapter(userTagAdapter);
     return true;
    }

    private void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }
}
