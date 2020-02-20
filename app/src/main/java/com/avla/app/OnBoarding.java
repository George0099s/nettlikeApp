package com.avla.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.avla.app.adapter.ViewPagerAdapter;
import com.avla.app.authorization.RegistrationActivity;
import com.avla.app.view.onboarding.FirstOnBoardingFragment;
import com.avla.app.view.onboarding.SecondOnBoardingFragment;
import com.avla.app.view.onboarding.ThirdOnBoardingFragment;
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
        viewPager.addOnPageChangeListener(listener);
        mNext = findViewById(R.id.next_tv);
        mSkip = findViewById(R.id.skip_tv);
        mNext.setOnClickListener(this::onClick);
        mSkip.setOnClickListener(this::onClick);
    }
    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position < 2) {
                mNext.setText("Next");
            }

            if (position == 2) {
                mNext.setText("Registration");
                mNext.setOnClickListener(OnBoarding.this::goToRegistration);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private void onClick(View v){
        switch (v.getId()){
            case R.id.skip_tv:
                Intent intent = new Intent(OnBoarding.this, RegistrationActivity.class);
                intent.putExtra("token", getIntent().getStringExtra("token"));
                startActivity(intent);
                break;
             case R.id.next_tv:
                 viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                 if (viewPager.getCurrentItem() == 2){
                     mNext.setText("registration");
                     mNext.setOnClickListener(this::goToRegistration);
                 } else if(viewPager.getCurrentItem() < 3) {
                     mNext.setText("Next");
                 }
                break;

        }
    }

    private void goToRegistration(View view) {
        Intent intent = new Intent(OnBoarding.this, RegistrationActivity.class);
        intent.putExtra("token", getIntent().getStringExtra("token"));
        startActivity(intent);
    }

}
