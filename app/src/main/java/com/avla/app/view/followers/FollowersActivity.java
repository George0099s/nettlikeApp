package com.avla.app.view.followers;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.avla.app.R;
import com.avla.app.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class FollowersActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        initView();
    }

    private void initView() {
        type = getIntent().getStringExtra("type");

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new FollowersFragment(),"Followers");
        viewPagerAdapter.addFragment(new FollowingFragment(),"Following");
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.profile_viewpager);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setCurrentItem(0, true);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        switch (type){
            case "followers":
                viewPager.setCurrentItem(0);
                break;
            case "following":
                viewPager.setCurrentItem(1);
                break;
        }

    }
}
