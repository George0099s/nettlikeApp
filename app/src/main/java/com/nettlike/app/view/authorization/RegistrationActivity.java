package com.nettlike.app.view.authorization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.firebase.iid.FirebaseInstanceId;
import com.nettlike.app.Constants;
import com.nettlike.app.Interface.IServer;
import com.nettlike.app.MainActivity;
import com.nettlike.app.R;
import com.nettlike.app.data.AppDatabase;
import com.nettlike.app.data.TokenDao;
import com.nettlike.app.model.EmailPojo;
import com.nettlike.app.model.UserModel;
import com.nettlike.app.model.UserSingleton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = "RegistrationActivity";

    private EditText mEmailED, mPasswordED;
    private String mEmail, mPass, token;
    private TextView loginText;
    private Button mNextBtn, mGoToRegistrationBtn, mLogInBtn;
    private Boolean isExist;
    private SharedPreferences mPrefs;
    private UserSingleton user = UserSingleton.INSTANCE;
    private AppDatabase db;
    private TokenDao tokenDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
        getToken();
    }

    private void getToken() {
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "avla_DB")
                .allowMainThreadQueries()
                .build();
        tokenDao = db.tokenDao();
        token = tokenDao.getAll().get(0).getToken();
        Log.d(TAG, "getToken: " + token);
        db.close();
//        token = getIntent().getStringExtra("token");
    }

    private void initViews() {
        getToken();
        loginText = findViewById(R.id.login_text);
        mPrefs = getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);
        mEmailED = findViewById(R.id.email_ed);
        mPasswordED = findViewById(R.id.password_ed);
        mNextBtn = findViewById(R.id.next_btn);
        mGoToRegistrationBtn = findViewById(R.id.sing_up_btn);
        mLogInBtn = findViewById(R.id.log_in_btn);
        mGoToRegistrationBtn.setOnClickListener(this::onClick);
        mNextBtn.setOnClickListener(this::onClick);
        mLogInBtn.setOnClickListener(this::onClick);
        mEmailED.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEmailED.getText().toString().equals("")) {
                    mNextBtn.setVisibility(View.VISIBLE);
                    mLogInBtn.setVisibility(View.GONE);
                    loginText.setText(getResources().getString(R.string.registration_or_login_string));
                    mPasswordED.setVisibility(View.GONE);
                }
            }
        });
    }

    private void checkIfEmailRegistered(String token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create())// говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<EmailPojo> call = service.checkIfEmailRegistered(token, mEmail);
        call.enqueue(new Callback<EmailPojo>() {
            @Override
            public void onResponse(Call<EmailPojo> call, Response<EmailPojo> response) {
                EmailPojo object = response.body();
                if (object.getPayload() == null){
                    Toast.makeText(RegistrationActivity.this, object.getError(), Toast.LENGTH_SHORT).show();
                }else {
                    isExist = object.getPayload().getExists();

                    if (isEmailExist(isExist)) {
                        mNextBtn.setVisibility(View.GONE);
                        mLogInBtn.setVisibility(View.VISIBLE);
                        loginText.setText(getResources().getString(R.string.login_string));
                        mPasswordED.setVisibility(View.VISIBLE);
                    } else {
                        mNextBtn.setVisibility(View.GONE);
                        mPasswordED.setVisibility(View.VISIBLE);
                        mPasswordED.requestFocus();
                        loginText.setText(getResources().getString(R.string.registration_string));
                        mGoToRegistrationBtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<EmailPojo> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void signUpWithEmail(String email, String password){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<EmailPojo> call = service.signUpWithEmail(token, email, password);
        call.enqueue(new Callback<EmailPojo>() {
            @Override
            public void onResponse(Call<EmailPojo> call, Response<EmailPojo> response) {
                EmailPojo object = response.body();
                if(object.getOk()){
                    Intent intent = new Intent(RegistrationActivity.this, SignUp.class);
                    mPrefs.edit().putBoolean("isLogIn", true).apply();
                    user.setExist(false);
                    intent.putExtra("token", token);
                    sendId(token);
                    startActivity(intent);                }
            }

            @Override
            public void onFailure(Call<EmailPojo> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());

            }
        });
    }

    private void logInWithEmail(String email, String password){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<EmailPojo> call = service.loginWithEmail(token, email, password);
        call.enqueue(new Callback<EmailPojo>() {
            @Override
            public void onResponse(Call<EmailPojo> call, Response<EmailPojo> response) {
                EmailPojo object = response.body();
                if(object.getOk()){
                    Toast.makeText(RegistrationActivity.this, "You logged in", Toast.LENGTH_SHORT).show();
                    user.setExist(true);
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    mPrefs.edit().putBoolean("isLogIn", true).apply();
                    intent.putExtra("token", token);
                    sendId(token);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegistrationActivity.this, object.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EmailPojo> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());

            }
        });
    }

    private void sendId(String token) {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String key = task.getResult().getToken();
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                                .build();

                        IServer service = retrofit.create(IServer.class);
                        Call<UserModel> call = service.sendRegistartionKey(token, key);
                        call.enqueue(new Callback<UserModel>() {
                            @Override
                            public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                            }

                            @Override
                            public void onFailure(Call<UserModel> call, Throwable t) {
                                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());

                            }
                        });
                    });




    }

    private Boolean isEmailExist(Boolean isExist) {
        this.isExist = isExist;
        return  isExist;
    }
    private void onClick(View v){

        mEmail = mEmailED.getText().toString();
        mPass = mPasswordED.getText().toString();
        switch (v.getId()){
           case R.id.next_btn:
               isEmailCorrect(mEmailED);
               break;
           case R.id.sing_up_btn:
               signUpWithEmail(mEmail, mPass);
               break;
           case R.id.log_in_btn:
               if (!mPass.isEmpty() && !mEmail.isEmpty())
                   logInWithEmail(mEmail, mPass);
                break;
       }
    }

    private void isEmailCorrect(EditText emailED) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        CharSequence inputStr = emailED.getText();
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            checkIfEmailRegistered(token);
        }  else {
            emailED.requestFocus();
            emailED.setError("Invalid email");
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
