package com.avla.app.Authorization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import com.avla.app.Adapter.ViewPagerAdapter;
import com.avla.app.Constants;
import com.avla.app.Database.AppDatabase;
import com.avla.app.Entity.TokenEntity;
import com.avla.app.Fragments.SignUp.SignUpAboutYourselfFragment;
import com.avla.app.Fragments.SignUp.SignUpChooseTagsFragment;
import com.avla.app.Fragments.SignUp.SignUpLocationFragment;
import com.avla.app.Fragments.SignUp.SignUpNameFragment;
import com.avla.app.Interface.IServer;
import com.avla.app.Interface.TokenDao;
import com.avla.app.MainActivity;
import com.avla.app.Model.User;
import com.avla.app.Model.UserPayload;
import com.avla.app.R;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity {

    private static final String TAG = "SignUp";
    private SharedPreferences sharedPreferences;
    private String firstName, lastName, token, aboutYourse1f, jobTitle;
    public static JSONArray tagList;
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
        tagList = new JSONArray();
        sharedPreferences = getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);
        firstName = sharedPreferences.getString("first name", "");
        lastName = sharedPreferences.getString("last name", "");
        aboutYourse1f = sharedPreferences.getString("about yourself","");
        jobTitle = sharedPreferences.getString("job title", "");
        tagList = new JSONArray();

        AppDatabase db = Room.databaseBuilder(this.getApplicationContext(),
                AppDatabase.class, "avlaDB")
                .allowMainThreadQueries()
                .build();

        TokenDao tokenDao = db.tokenDao();
        List<TokenEntity> s = tokenDao.getToken();
        token = getIntent().getStringExtra("token");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < 3) {
                    mNext.setText("Next");
                }

                if (position == 3) {
                    mNext.setText("Finish");
                    mNext.setOnClickListener(SignUp.this::finish);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void onClick(View view){
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
        if (viewPager.getCurrentItem() == 3){
            mNext.setText("finish");
            mNext.setOnClickListener(this::finish);
        } else {
            mNext.setText("Next");
        }
    }


    private void finish(View view){
        Toast.makeText(this, tagList.toString(), Toast.LENGTH_SHORT).show();
        sendUserData(token,firstName, lastName, aboutYourse1f, jobTitle, tagList);

    }

    private void sendUserData(String token, String firstName, String lastName,String aboutYorself,String jobTitle, JSONArray tagList) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.getavla.com/") // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<User> call = service.sendUserData(token, firstName, lastName,aboutYorself, jobTitle, tagList);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User object = response.body();
                UserPayload userPayload = object.getPayload();
                if(object.getOk() == true){
                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                    intent.putExtra("token", token);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignUp.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());

            }
        });
    }


}
