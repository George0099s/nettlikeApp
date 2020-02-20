package com.avla.app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.avla.app.authorization.RegistrationActivity;
import com.avla.app.model.UserSingleton;
import com.avla.app.view.main.AddContentFragment;
import com.avla.app.view.main.MessageFragment;
import com.avla.app.view.main.PeopleFragment;
import com.avla.app.view.main.PostFragment;
import com.avla.app.view.main.ProfileFragment;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Socket socket;
    private Manager manager;
    private JSONObject tokenJson = new JSONObject();
    private AvlaApplication app;
    private UserSingleton user = UserSingleton.INSTANCE;
    private Dialog dialog;
    private Fragment profileFragment;
    private Fragment messageFragment;
    private Fragment addContentFragment;
    final Fragment peopleFragment = new PeopleFragment();
    private Fragment postFragment;
    private String token;
    private Button goToRegDia;
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = peopleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!user.getExist()) {
            fm.beginTransaction().replace(R.id.container, peopleFragment, "3").commit();
        }
    }

    private void initViews() {
        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.people);
        goToRegDia = findViewById(R.id.go_to_registration123);
        if (!user.getExist()) {
            fm.beginTransaction().replace(R.id.container, peopleFragment, "3").commit();
        }

        dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setTitle(R.string.login_or_create_string);
        dialog.setContentView(R.layout.dialog_view);
        dialog.create();
        if (user.getExist()) {
            profileFragment = new ProfileFragment();
            messageFragment = new MessageFragment();
            postFragment = new PostFragment();
            addContentFragment = new AddContentFragment();
            fm.beginTransaction().add(R.id.container, addContentFragment, "5").hide(addContentFragment).commit();
            fm.beginTransaction().add(R.id.container, postFragment, "4").hide(postFragment).commit();
            fm.beginTransaction().add(R.id.container, messageFragment, "2").hide(messageFragment).commit();
            fm.beginTransaction().add(R.id.container, profileFragment, "1").hide(profileFragment).commit();
        }
        token = getIntent().getStringExtra("token");

        app = (AvlaApplication) getApplication();
        manager = app.getManager();
        socket = manager.socket("/all");
        try {
            tokenJson.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("abc", "initViews: token" + tokenJson.toString());
        socket.emit("create_connection", tokenJson);
        socket.connect();
        fm.beginTransaction().add(R.id.container, peopleFragment, "3").commit();
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.people:
                fm.beginTransaction().hide(active).show(peopleFragment).commit();
                active = peopleFragment;
                return true;

            case R.id.posts:
                if (postFragment != null && UserSingleton.INSTANCE.getExist()) {
                    fm.beginTransaction().hide(active).show(postFragment).commit();
                    active = postFragment;
                } else {
                    dialog.show();
                }
                return true;
            case R.id.messages:
                if (user.getExist()) {
                    fm.beginTransaction().hide(active).show(messageFragment).commit();
                    active = messageFragment;
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
}
