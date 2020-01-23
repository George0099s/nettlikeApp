package com.avla.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.avla.app.fragments.Main.MessageFragment;
import com.avla.app.fragments.Main.PeopleFragment;
import com.avla.app.fragments.Main.ProfileFragment;
import com.avla.app.fragments.Main.QuestionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initViews();
    }

    private void initViews() {
        BottomNavigationView navigation =  findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.profile);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.questions:
                loadFragment(QuestionFragment.newInstance());
                return true;
            case R.id.people:
                loadFragment(PeopleFragment.newInstance());
                return true;
            case R.id.messages:
                loadFragment(MessageFragment.newInstance());
                return true;
            case R.id.profile:
                loadFragment(ProfileFragment.newInstance());
                return true;
        }

        return false;
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.commit();
    }
}
