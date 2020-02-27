package com.nettlike.app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nettlike.app.activity.ActivitiesFragment;
import com.nettlike.app.data.AppDatabase;
import com.nettlike.app.data.TokenDao;
import com.nettlike.app.model.UserSingleton;
import com.nettlike.app.people.ui.PeopleFragment;
import com.nettlike.app.posts.PostFragment;
import com.nettlike.app.profile.ui.ProfileFragment;
import com.nettlike.app.view.authorization.RegistrationActivity;
import com.nettlike.app.view.main.AddContentFragment;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private UserSingleton user = UserSingleton.INSTANCE;
    private Dialog dialog;
    private Fragment profileFragment;
    private Fragment activitiesFragment;
    private Fragment addContentFragment;
    final Fragment peopleFragment = new PeopleFragment();
    private Fragment postFragment = new PostFragment();
    private String token;
    private Button goToRegDia;
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = peopleFragment;
    private TokenDao tokenDao;
    private AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.people:
                if (active != peopleFragment)
                fm.beginTransaction().hide(active).show(peopleFragment).commit();
                active = peopleFragment;
                return true;
            case R.id.posts:
                if (active != postFragment)
                    fm.beginTransaction().hide(active).show(postFragment).commit();
                    active = postFragment;
                return true;
            case R.id.activities:
                if (user.getExist()) {
                    fm.beginTransaction().hide(active).show(activitiesFragment).commit();
                    active = activitiesFragment;
                } else {
                    dialog.show();
                }
                return true;
            case R.id.profile:
                if (user.getExist()) {
                    fm.beginTransaction().hide(active).show(profileFragment).commit();
                    active = profileFragment;
                } else {
                    dialog.show();
                }
                return true;
            case R.id.add_content:
                if (user.getExist()) {
                    fm.beginTransaction().hide(active).show(addContentFragment).commit();
                    active = addContentFragment;
                } else {
                    dialog.show();
                }
            return true;
        }

        return false;
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (!user.getExist()) {
//            fm.beginTransaction().replace(R.id.container, peopleFragment, "3").commit();
//            fm.beginTransaction().add(R.id.container, postFragment, "4").commit();

//            profileFragment = null;
//            activitiesFragment = null;
//            addContentFragment = null;
        } else {
//            profileFragment = new ProfileFragment();
//            activitiesFragment = new ActivitiesFragment();
//            addContentFragment = new AddContentFragment();
//
//            fm.beginTransaction().add(R.id.container, addContentFragment, "5").hide(addContentFragment).commit();
//            fm.beginTransaction().add(R.id.container, activitiesFragment, "2").hide(activitiesFragment).commit();
//            fm.beginTransaction().add(R.id.container, profileFragment, "1").hide(profileFragment).commit();
        }
    }

    public void onclick(View view) {
        Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initViews() {

        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.people);
        goToRegDia = findViewById(R.id.go_to_registration123);
        dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setTitle(R.string.login_or_create_string);
        dialog.setContentView(R.layout.dialog_view);
        dialog.create();

        if (user.getExist()) {
            profileFragment = new ProfileFragment();
            activitiesFragment = new ActivitiesFragment();
            addContentFragment = new AddContentFragment();
            fm.beginTransaction().add(R.id.container, addContentFragment, "5").hide(addContentFragment).commit();
            fm.beginTransaction().add(R.id.container, activitiesFragment, "2").hide(activitiesFragment).commit();
            fm.beginTransaction().add(R.id.container, profileFragment, "1").hide(profileFragment).commit();
        }

        fm.beginTransaction().add(R.id.container, peopleFragment, "3").show(peopleFragment).commit();
        fm.beginTransaction().add(R.id.container, postFragment, "4").hide(postFragment).commit();

    }
}
