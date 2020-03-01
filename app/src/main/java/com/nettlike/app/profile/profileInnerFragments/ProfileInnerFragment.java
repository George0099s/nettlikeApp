package com.nettlike.app.profile.profileInnerFragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nettlike.app.R;
import com.nettlike.app.model.UserSingleton;
import com.nettlike.app.profile.UserTagAdapter;
import com.nettlike.app.profile.data.ProfileRepository;
import com.nettlike.app.profile.utils.ProfileViewModelFactory;
import com.nettlike.app.profile.viewmodel.ProfileViewModel;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileInnerFragment extends Fragment {

    private static final String TAG = "ProfileInnerFragment";
    private ImageView sendEmail, sendTwitter, sendFacebook;
    private TextView userJob, aboutUser;
    private RecyclerView tagRecycler;
    private UserTagAdapter userTagAdapter;
    private JSONArray tagsJson;
    private UserSingleton user = UserSingleton.INSTANCE;
    private ProfileRepository profileRepository;
    private ProfileViewModel profileViewModel;

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

    private Observer<UserSingleton> profileListUpdateObserver = userSingleton -> {
        JSONArray tagsJsonArray = new JSONArray();
        ArrayList<String> tags = userSingleton.getTagsName();
        for (int i = 0; i < tags.size(); i++) {
            tagsJsonArray.put(tags.get(i));
        }
        if (userSingleton.getTwitterURL().isEmpty())
            sendTwitter.setVisibility(View.GONE);
        if (userSingleton.getFacebookURL().isEmpty())
            sendFacebook.setVisibility(View.GONE);
        userJob.setText(userSingleton.getJobTitle());
        aboutUser.setText(userSingleton.getAboutMyself());
        userTagAdapter = new UserTagAdapter(tagsJsonArray, getContext());
        tagRecycler.setAdapter(userTagAdapter);
    };

    private void initViews(View view) {
        sendFacebook = view.findViewById(R.id.send_facebook);
        sendTwitter = view.findViewById(R.id.send_twitter);
        sendTwitter.setOnClickListener(this::onClick);
        sendFacebook.setOnClickListener(this::onClick);
        tagRecycler = view.findViewById(R.id.tag_recycler);
        tagRecycler.setHasFixedSize(true);
        tagRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        userJob = view.findViewById(R.id.user_job);
        aboutUser = view.findViewById(R.id.aboutUser);
        sendEmail = view.findViewById(R.id.send_email);
        sendEmail.setOnClickListener(this::onClick);
//        getUserInfo();
        profileRepository = new ProfileRepository(getContext());
        profileViewModel = ViewModelProviders.of(getActivity(), new ProfileViewModelFactory(profileRepository)).get(ProfileViewModel.class);
        profileViewModel.getProfileMutableLiveData().observe(getActivity(), profileListUpdateObserver);
    }

    @Override
    public void onResume() {
        super.onResume();
//        getUserInfo();
        profileViewModel.getProfileMutableLiveData().observe(getActivity(), profileListUpdateObserver);
        if (user.getTwitterURL().isEmpty() || user.getTwitterURL() == null)
            sendTwitter.setVisibility(View.GONE);
        if (user.getFacebookURL().isEmpty() || user.getFacebookURL() == null)
            sendFacebook.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        profileViewModel.getProfileMutableLiveData().removeObserver(profileListUpdateObserver);
        super.onStop();
    }

    @Override
    public void onPause() {
        profileViewModel.getProfileMutableLiveData().removeObserver(profileListUpdateObserver);
        super.onPause();
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
        switch (view.getId()) {
            case R.id.send_email:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, user.getEmail());
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
                startActivity(Intent.createChooser(intent, "Send Email"));
                break;
            case R.id.send_facebook:
                Intent openlink = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.facebook_link) + Uri.parse(user.getFacebookURL())));
                startActivity(Intent.createChooser(openlink, "Browser"));
                break;
            case R.id.send_twitter:
                Intent openlinkTwitter = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.twitter_link) + Uri.parse(user.getTwitterURL())));
                startActivity(Intent.createChooser(openlinkTwitter, "Browser"));
                break;
        }
    }
}
