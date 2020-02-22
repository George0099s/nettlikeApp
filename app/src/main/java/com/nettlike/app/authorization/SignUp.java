package com.nettlike.app.authorization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.nettlike.app.Constants;
import com.nettlike.app.Interface.IServer;
import com.nettlike.app.MainActivity;
import com.avla.app.R;
import com.nettlike.app.adapter.ViewPagerAdapter;
import com.nettlike.app.view.signUp.SignUpAboutYourselfFragment;
import com.nettlike.app.view.signUp.SignUpChooseTagsFragment;
import com.nettlike.app.view.signUp.SignUpLocationFragment;
import com.nettlike.app.view.signUp.SignUpNameFragment;
import com.nettlike.app.model.UserModel;
import com.nettlike.app.model.UserPayload;
import com.nettlike.app.model.UserSingleton;

import org.json.JSONArray;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity {

    private static final String TAG = "SignUp";
    private SharedPreferences mPrefs;
    private String firstName, lastName, token, aboutYourse1f, jobTitle, country, city, age;
    private ViewPager viewPager;
    private Button mNext;
    private UserSingleton user;
    private JSONArray tagList;

    @Inject
    public SignUp(){

    }

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
        mPrefs = getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);
//        AppDatabase db = Room.databaseBuilder(this.getApplicationContext(),
//                AppDatabase.class, "avlaDB")
//                .allowMainThreadQueries()
//                .build();
//
//        TokenDao tokenDao = db.tokenDao();
//        List<TokenEntity> s = tokenDao.getToken();
        token = getIntent().getStringExtra("token");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < 3) {
                    mNext.setText("Next");
                    mNext.setOnClickListener(SignUp.this::onClick);
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
        UserSingleton user = UserSingleton.INSTANCE;
        firstName = user.getFirstName();
        lastName = user.getLastName();
        aboutYourse1f = user.getAboutMyself();
        jobTitle = user.getJobTitle();
        city = user.getCity();
        country = user.getCountry();
        age = user.getAge();
        tagList = user.getTagList();
        sendUserData(token,firstName, lastName,age, aboutYourse1f,country, city, jobTitle, tagList);
    }

    private void sendUserData(String token, String firstName, String lastName, String age, String aboutYorself, String country, String city, String jobTitle, JSONArray tagList) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<UserModel> call = service.sendUserData(token, firstName, lastName,age,aboutYorself, country, city, jobTitle, tagList);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel object = response.body();
                UserPayload userPayload = object.getPayload();
                Log.d("222", "onResponse: " + object.getOk());
                if(object.getOk()){
                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                    mPrefs.edit().putBoolean("isLogIn", true).apply();
                    user.setExist(true);
                    intent.putExtra("token", token);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignUp.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());

            }
        });
    }


}
