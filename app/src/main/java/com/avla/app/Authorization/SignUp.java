package com.avla.app.Authorization;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import com.avla.app.Adapter.ViewPagerAdapter;
import com.avla.app.Database.AppDatabase;
import com.avla.app.Entity.TokenEntity;
import com.avla.app.Fragments.SignUp.SignUpAboutYourselfFragment;
import com.avla.app.Fragments.SignUp.SignUpChooseTagsFragment;
import com.avla.app.Fragments.SignUp.SignUpLocationFragment;
import com.avla.app.Fragments.SignUp.SignUpNameFragment;
import com.avla.app.Interface.SignUpInterface;
import com.avla.app.Interface.TokenDao;
import com.avla.app.R;

import java.util.HashMap;
import java.util.List;

public class SignUp extends AppCompatActivity {

    private static final String TAG = "SignUp";
    private ViewPager viewPager;
    private Button mNext;
    private HashMap<String, String> user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        initViews();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new SignUpNameFragment());
        viewPagerAdapter.addFragment(new SignUpAboutYourselfFragment());
        viewPagerAdapter.addFragment(new SignUpLocationFragment());
        viewPagerAdapter.addFragment(new SignUpChooseTagsFragment());

        viewPager.setAdapter(viewPagerAdapter);

    }

    private void initViews() {
        mNext = findViewById(R.id.next_btn);
        mNext.setOnClickListener(this::onClick);
        viewPager = findViewById(R.id.viewpager);

        AppDatabase db = Room.databaseBuilder(this.getApplicationContext(),
                AppDatabase.class, "avlaDB")
                .allowMainThreadQueries()
                .build();

        TokenDao tokenDao = db.tokenDao();
        List<TokenEntity> s = tokenDao.getToken();
    }
    private void onClick(View view){
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
    }
}
