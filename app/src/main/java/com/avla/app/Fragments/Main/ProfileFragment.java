package com.avla.app.Fragments.Main;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.avla.app.Adapter.ViewPagerAdapter;
import com.avla.app.Fragments.Main.ProfileInnerFragments.ProfileInnerFragment;
import com.avla.app.Fragments.Onboarding.SecondOnBoardingFragment;
import com.avla.app.Fragments.Onboarding.ThirdOnBoardingFragment;
import com.avla.app.Interface.IServer;
import com.avla.app.Model.User;
import com.avla.app.Model.UserPayload;
import com.avla.app.R;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private TextView userName;
    private GetUserInfo getUserInfoTask;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private PagerAdapter pagerAdapter;
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
        FragmentManager fragmentManager = getFragmentManager();
        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.profile_viewpager);
        viewPagerAdapter = new ViewPagerAdapter(fragmentManager);

        viewPagerAdapter.addFragment(new ProfileInnerFragment(),"Profile");
        viewPagerAdapter.addFragment(new SecondOnBoardingFragment(),"Questions");
        viewPagerAdapter.addFragment(new ThirdOnBoardingFragment(),"Answers");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        initViews(view);
        return view;
    }

    private void initViews(View view) {

        getUserInfoTask = new GetUserInfo();
        getUserInfoTask.execute();
        userName = view.findViewById(R.id.user_name);
    }


    private class GetUserInfo extends AsyncTask<String, Void, Void>{
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        String token = getActivity().getIntent().getStringExtra("token");
        @Override
        protected Void doInBackground(String... strings) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.getavla.com/") // Адрес сервера
                    .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                    .build();

            IServer service = retrofit.create(IServer.class);
            Call<User> call = service.getUserInfo(token);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    User object = response.body();
                    UserPayload userPayload = object.getPayload();
                    Log.d(TAG, "onResponse: name" + userPayload.getFirstName() + userPayload.getLastName()+ " job "+userPayload.getJobTitle());
                    userName.setText(userPayload.getFirstName() + " " + userPayload.getLastName() );

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
                }
            });

            return null;
        }
    }
}
