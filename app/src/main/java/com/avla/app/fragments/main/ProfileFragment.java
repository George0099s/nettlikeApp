package com.avla.app.fragments.main;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.adapter.ViewPagerAdapter;
import com.avla.app.fragments.main.profileInnerFragments.AnswerInnerFragment;
import com.avla.app.fragments.main.profileInnerFragments.EditUserProfileActivity;
import com.avla.app.fragments.main.profileInnerFragments.EventInnerFragment;
import com.avla.app.fragments.main.profileInnerFragments.ProfileInnerFragment;
import com.avla.app.fragments.main.profileInnerFragments.QuestionInnerFragment;
import com.avla.app.model.User;
import com.avla.app.model.UserPayload;
import com.avla.app.model.UserSingleton;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private static final Scheduler THREAD_2 = Schedulers.newThread();
    private String token;
    private ImageView editProfile, userImage;
    private TextView userName;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;
    public ProfileFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new ProfileFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);

        return view;
    }

    private void initViews(View view) {
//        FragmentManager fragmentManager = getFragmentManager();
        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.profile_viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ProfileInnerFragment(),"Profile");
        viewPagerAdapter.addFragment(new QuestionInnerFragment(),"Questions");
        viewPagerAdapter.addFragment(new AnswerInnerFragment(),"Answers");
        viewPagerAdapter.addFragment(new EventInnerFragment(),"Events");
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0, true);
        viewPager.setAdapter(viewPagerAdapter);
        userImage = view.findViewById(R.id.user_image);
        tabLayout.setupWithViewPager(viewPager);
        editProfile = view.findViewById(R.id.edit_profile);
        editProfile.setOnClickListener(this::onClick);
        userName = view.findViewById(R.id.job_title);
        token = getActivity().getIntent().getStringExtra("token");

        Observable.just((getUserInfo()))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
    }

    private Object getUserInfo() {
        String token = getActivity().getIntent().getStringExtra("token");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<User> call = service.getUserInfo(token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User object = response.body();
                UserPayload userPayload = object.getPayload();
                UserSingleton user = UserSingleton.INSTANCE;
                user.setUserId(userPayload.getId());
                userName.setText(userPayload.getFirstName() + " " + userPayload.getLastName() );
                Glide.with(ProfileFragment.this).load(Constants.BASIC_URL + "public_api/account/get_picture?picture_id="+userPayload.getPictureId()).into(userImage);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });
    return null;
    }

    private void onClick(View view) {
        Intent intent = new Intent(getContext(), EditUserProfileActivity.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }
}
