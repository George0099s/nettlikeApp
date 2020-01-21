package com.avla.app.Authorization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.MainActivity;
import com.avla.app.Model.EmailPojo;
import com.avla.app.R;

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
    private Button mNextBtn, mGoToRegistrationBtn, mLogInBtn;
    private Boolean isExist;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
        getToken();
    }

    private void getToken() {
        token = getIntent().getStringExtra("token");
    }

    private void initViews() {
        getToken();
        sharedPreferences = getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);
        mEmailED = findViewById(R.id.email_ed);
        mPasswordED = findViewById(R.id.password_ed);
        mNextBtn = findViewById(R.id.next_btn);
        mGoToRegistrationBtn = findViewById(R.id.sing_up_btn);
        mLogInBtn = findViewById(R.id.log_in_btn);
        mGoToRegistrationBtn.setOnClickListener(this::onClick);
        mNextBtn.setOnClickListener(this::onClick);
        mLogInBtn.setOnClickListener(this::onClick);
    }

    private void checkIfEmailRegistered(String token){
        Log.d(TAG, "checkIfEmailRegistered: +" + token);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.getavla.com/") // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create())// говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<EmailPojo> call = service.checkIfEmailRegistered(token, mEmail);
        call.enqueue(new Callback<EmailPojo>() {
            @Override
            public void onResponse(Call<EmailPojo> call, Response<EmailPojo> response) {
                EmailPojo object = response.body();
                isExist = object.getPayload().getExists();

                if(isEmailExist(isExist)){
                    mNextBtn.setVisibility(View.GONE);
                    mLogInBtn.setVisibility(View.VISIBLE);
                    mPasswordED.setVisibility(View.VISIBLE);
                } else {
                    mNextBtn.setVisibility(View.GONE);
                    mPasswordED.setVisibility(View.VISIBLE);
                    mGoToRegistrationBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<EmailPojo> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void signUpWithEmail(String email, String password){
        String token = getIntent().getStringExtra("token");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.getavla.com/") // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<EmailPojo> call = service.signUpWithEmail(token, email, password);
        call.enqueue(new Callback<EmailPojo>() {
            @Override
            public void onResponse(Call<EmailPojo> call, Response<EmailPojo> response) {
                EmailPojo object = response.body();
                if(object.getOk()){
                    Toast.makeText(RegistrationActivity.this, "Email and pass added", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistrationActivity.this, SignUp.class);
                    intent.putExtra("token", token);
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
                .baseUrl("https://api.getavla.com/") // Адрес сервера
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
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    intent.putExtra("token", token);
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
                logInWithEmail(mEmail, mPass);
                break;
       }
    }

    private void isEmailCorrect(EditText emailED) {
        String token = getIntent().getStringExtra("token");
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

//    private class GetToken extends AsyncTask<Void, Void, String> {
//
//
//        @Override
//        protected String doInBackground(Void... voids) {
//
//            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//            String URL = "https://api.getavla.com/public_api/auth/create_token";
//            JsonObjectRequest objectRequest = new JsonObjectRequest(
//                    Request.Method.POST,
//                    URL,
//                    null,
//                    response -> {
//                        try {
//                            JSONObject object = response.getJSONObject("payload");
//                            object = response.getJSONObject("payload");
//                            token = object.getString("token");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    },
//                    error -> {
//                        System.out.println(error);
//                        Log.e("response error", error.toString());
//                    }
//            );
//            requestQueue.add(objectRequest);
//
//            return token;
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
