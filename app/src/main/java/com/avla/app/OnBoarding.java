package com.avla.app;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.avla.app.Fragments.FirstOnBoardingFragment;
import com.avla.app.Fragments.SecondOnBoardingFragment;
import com.avla.app.Fragments.ThirdOnBoardingFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

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
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        mNext = findViewById(R.id.next_tv);
        mSkip = findViewById(R.id.skip_tv);
        mNext.setOnClickListener(this::onClick);
        mSkip.setOnClickListener(this::onClick);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new FirstOnBoardingFragment(),"o");
        viewPagerAdapter.addFragment(new SecondOnBoardingFragment(),"o");
        viewPagerAdapter.addFragment(new ThirdOnBoardingFragment(),"o");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void onClick(View v){
        switch (v.getId()){
            case R.id.skip_tv:
                startActivity(new Intent(OnBoarding.this, MainActivity.class));
                break;
             case R.id.next_tv:
                 viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                break;

        }
    }
    private class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;
        private ArrayList<CharSequence> titles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        private void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
