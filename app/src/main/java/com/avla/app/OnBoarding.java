package com.avla.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.avla.app.Adapter.ViewPagerAdapter;
import com.avla.app.Authorization.RegistrationActivity;
import com.avla.app.Fragments.Onboarding.FirstOnBoardingFragment;
import com.avla.app.Fragments.Onboarding.SecondOnBoardingFragment;
import com.avla.app.Fragments.Onboarding.ThirdOnBoardingFragment;
import com.google.android.material.tabs.TabLayout;

public class OnBoarding extends FragmentActivity {
    private static final String TAG = "OnBoarding";

    ViewPager viewPager;
    TabLayout tabLayout;
    PagerAdapter pagerAdapter;
    private TextView mNext, mSkip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        initViews();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new FirstOnBoardingFragment(),"o");
        viewPagerAdapter.addFragment(new SecondOnBoardingFragment(),"o");
        viewPagerAdapter.addFragment(new ThirdOnBoardingFragment(),"o");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initViews() {
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        mNext = findViewById(R.id.next_tv);
        mSkip = findViewById(R.id.skip_tv);
        mNext.setOnClickListener(this::onClick);
        mSkip.setOnClickListener(this::onClick);
    }

    private void onClick(View v){
        switch (v.getId()){
            case R.id.skip_tv:
                startActivity(new Intent(OnBoarding.this, RegistrationActivity.class));
                break;
             case R.id.next_tv:
                 viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                break;

        }
    }

}
