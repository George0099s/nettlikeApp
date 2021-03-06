package com.nettlike.app.profile.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.nettlike.app.R;
import com.nettlike.app.adapter.ViewPagerAdapter;
import com.nettlike.app.data.AppDatabase;
import com.nettlike.app.data.TokenDao;
import com.nettlike.app.model.UserSingleton;
import com.nettlike.app.profile.data.ProfileRepository;
import com.nettlike.app.profile.profileInnerFragments.EditUserProfileActivity;
import com.nettlike.app.profile.profileInnerFragments.PostsInnerFragment;
import com.nettlike.app.profile.profileInnerFragments.ProfileInnerFragment;
import com.nettlike.app.profile.utils.ProfileViewModelFactory;
import com.nettlike.app.profile.viewmodel.ProfileViewModel;
import com.nettlike.app.view.followers.ui.FollowersActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private String token;
    private ImageView editProfile, userImage, activity;
    private TextView userName, userLocation, followers, following, publication;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private View followersSector, followingSector;
    private ViewPagerAdapter viewPagerAdapter;
    private UserSingleton user = UserSingleton.INSTANCE;
    private ProfileViewModel profileViewModel;
    private ProfileRepository profileRepository;
    private AppDatabase db;
    private TokenDao tokenDao;
    private Observer<UserSingleton> profileListUpdateObserver = userSingleton -> {
        userName.setText(String.format("%s %s", userSingleton.getFirstName(), userSingleton.getLastName()));
        userLocation.setText(String.format("%s, %s", userSingleton.getCountry(), userSingleton.getCity()));
        publication.setText(String.valueOf(userSingleton.getCount_publication()));
        followers.setText(String.valueOf(userSingleton.getFollowers().size()));
        following.setText(String.valueOf(userSingleton.getFollowing().size()));
        user.setExist(true);
        Glide.with(getContext()).load(userSingleton.getPictureURL()).apply(RequestOptions.circleCropTransform()).into(userImage);
    };

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
//        db = Room.databaseBuilder(getContext(),
//                AppDatabase.class, "avla_DB")
//                .allowMainThreadQueries()
//                .build();
//        tokenDao = db.tokenDao();
//        token = tokenDao.getToken().get(0).getToken();
//        db.close();
        token = getActivity().getIntent().getStringExtra("token");
        followingSector = view.findViewById(R.id.following_sector);
        followingSector.setOnClickListener(this::onClick);
        followersSector = view.findViewById(R.id.followers_sector);
        followersSector.setOnClickListener(this::onClick);
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ProfileInnerFragment(), "Profile");
        viewPagerAdapter.addFragment(new PostsInnerFragment(), "Posts");
        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.profile_viewpager);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(0, true);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        userImage = view.findViewById(R.id.user_image);
        editProfile = view.findViewById(R.id.edit_profile);
        editProfile.setOnClickListener(this::onClick);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following_text_view);
        publication = view.findViewById(R.id.publications_text_view);
        userName = view.findViewById(R.id.follower_username);
        userLocation = view.findViewById(R.id.user_locatoin);
        profileRepository = new ProfileRepository(getContext());
        profileViewModel = ViewModelProviders.of(getActivity(), new ProfileViewModelFactory(profileRepository)).get(ProfileViewModel.class);
        profileViewModel.getProfileMutableLiveData().observe(getActivity(), profileListUpdateObserver);
    }

    @Override
    public void onResume() {
        super.onResume();
//        profileRepository = new ProfileRepository(getContext());
//        profileViewModel = ViewModelProviders.of(getActivity(), new ProfileViewModelFactory(profileRepository)).get(ProfileViewModel.class);
//        profileViewModel.getProfileMutableLiveData().postValue(UserSingleton.INSTANCE);

        profileViewModel.getProfileMutableLiveData().observe(getActivity(), profileListUpdateObserver);

    }


    @Override
    public void onStop() {
        profileViewModel.getProfileMutableLiveData().removeObserver(profileListUpdateObserver);
        super.onStop();

    }

    //    @Override
//    public void onResume() {
//        super.onResume();
//           if (user.getExist()) {
//               Observable.just((getUserInfo()))
//                       .subscribeOn(Schedulers.io())
//                       .observeOn(AndroidSchedulers.mainThread())
//                       .subscribe();
//           }
//    }
//
//    private Object getUserInfo() {
//
//        String token = getActivity().getIntent().getStringExtra("token");
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constants.BASIC_URL) // Адрес сервера
//                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
//                .build();
//
//        UserInfoAPI service = retrofit.create(UserInfoAPI.class);
//        Call<UserModel> call = service.getUserInfo(token);
//        call.enqueue(new Callback<UserModel>() {
//            @Override
//            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                UserModel object = response.body();
//                UserPayload userPayload = object.getPayload();
//                ArrayList<String> tags = new ArrayList<>();
//                List<PayloadTag> payloadTags = userPayload.getTags();
//
//                for (int i = 0; i < payloadTags.size(); i++) {
//                    tags.add(payloadTags.get(i).getName());
//                }
//
//                user.setUserId(userPayload.getId());
//                user.setJobTitle(userPayload.getJobTitle());
//                user.setCountry(userPayload.getCountry());
//                user.setCity(userPayload.getCity());
//                user.setFirstName(userPayload.getFirstName());
//                user.setLastName(userPayload.getLastName());
//                user.setAboutMyself(userPayload.getDesctiption());
//                user.setAge(userPayload.getAge());
//                user.setEvents(userPayload.getEvents());
//                user.setSavedPosts(userPayload.getSavedPosts());
//                user.setPictureId(userPayload.getPictureUrl());
//                user.setSavedPeople(userPayload.getSavedPeople());
//                user.setCount_publication(userPayload.getPosts().size()+userPayload.getEvents().size());
//                user.setFollowers((ArrayList<String>) userPayload.getFollowers());
//                user.setFollowing((ArrayList<String>) userPayload.getFollowing());
//                user.setTagsName(tags);
//                userName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
//                userLocation.setText(String.format("%s, %s", user.getCountry(), user.getCity()));
//                publication.setText(String.valueOf(user.getCount_publication()));
//                followers.setText(String.valueOf(user.getFollowers().size()));
//                following.setText(String.valueOf(user.getFollowing().size()));
//                user.setExist(true);
//                Glide.with(getContext()).load(userPayload.getPictureUrl()).apply(RequestOptions.circleCropTransform()).into(userImage);
//            }
//
//            @Override
//            public void onFailure(Call<UserModel> call, Throwable t) {
//                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
//            }
//        });
//    return null;
//    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_profile:
                Intent intent = new Intent(getContext(), EditUserProfileActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
                break;
            case R.id.followers_sector:
                Intent intent2 = new Intent(getContext(), FollowersActivity.class);
                intent2.putExtra("token", token);
                intent2.putExtra("type", "followers");
                intent2.putExtra("account_id", user.getUserId());
                startActivity(intent2);
                break;
            case R.id.following_sector:
                Intent intent3 = new Intent(getContext(), FollowersActivity.class);
                intent3.putExtra("token", token);
                intent3.putExtra("type", "following");
                intent3.putExtra("account_id", user.getUserId());
                startActivity(intent3);
                break;
        }
    }
}
