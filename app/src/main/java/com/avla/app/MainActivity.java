package com.avla.app;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.avla.app.fragments.main.MessageFragment;
import com.avla.app.fragments.main.PeopleFragment;
import com.avla.app.fragments.main.ProfileFragment;
import com.avla.app.fragments.main.QuestionFragment;
import com.github.nkzawa.emitter.Emitter;
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
    final Fragment profileFragment = new ProfileFragment();
    final Fragment messageFragment = new MessageFragment();
    final Fragment peopleFragment = new PeopleFragment();
    final Fragment questionFragment = new QuestionFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = profileFragment;

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

        app = (AvlaApplication) getApplication();
        manager = app.getManager();
        socket = manager.socket("/all");
        try {
            tokenJson.put("token",getIntent().getStringExtra("token"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("abc", "initViews: token" + tokenJson.toString());
        socket.emit("create_connection", tokenJson);
        socket.on("create_connection_successful", onConnectionSuccessful);
        socket.connect();
        fm.beginTransaction().add(R.id.container, questionFragment, "4").hide(messageFragment).commit();
        fm.beginTransaction().add(R.id.container, peopleFragment, "3").hide(peopleFragment).commit();
        fm.beginTransaction().add(R.id.container, messageFragment, "2").hide(messageFragment).commit();
        fm.beginTransaction().add(R.id.container, profileFragment, "1").commit();
    }
    Emitter.Listener onConnectionSuccessful = args -> runOnUiThread(() -> {
        JSONObject data = (JSONObject) args[0];
        Log.d("abc", ": data" + data);
    });
    @Override
    public void onBackPressed() {
        finish();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.questions:
                fm.beginTransaction().hide(active).show(questionFragment).commit();
                active = questionFragment;
                return true;
            case R.id.people:
                fm.beginTransaction().hide(active).show(peopleFragment).commit();
                active = peopleFragment;
                return true;
            case R.id.messages:
                fm.beginTransaction().hide(active).show(messageFragment).commit();
                active = messageFragment;
                return true;
            case R.id.profile:
                fm.beginTransaction().hide(active).show(profileFragment).commit();
                active = profileFragment;
                return true;
        }

        return false;
    };

//    private void loadFragment(Fragment fragment) {
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.container, fragment);
//        ft.commit();
//    }
}
