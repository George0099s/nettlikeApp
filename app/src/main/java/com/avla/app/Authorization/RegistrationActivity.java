package com.avla.app.Authorization;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.avla.app.Interface.IServer;
import com.avla.app.Model.EmailValidate;
import com.avla.app.Model.PayloadModel;
import com.avla.app.Profile.ProfileActivity;
import com.avla.app.R;

import org.json.JSONException;
import org.json.JSONObject;

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
    private String mEmail, mPass, mToken;
    private Button mNextBtn, mGoToRegistrationBtn, mLogInBtn;
    private GetToken getToken;
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
//        if(sharedPreferences.getString("token",null) == null){
//            GetToken getToken
//            getToken.execute();
//            sharedPreferences.edit().putString("token", getToken.token);
//        }
//        getToken = new GetToken();
//        getToken.execute();
    sharedPreferences.getString("token", null);
    }

    private void initViews() {
        mEmailED = findViewById(R.id.email_ed);
        mPasswordED = findViewById(R.id.password_ed);
        mNextBtn = findViewById(R.id.next_btn);
        mGoToRegistrationBtn = findViewById(R.id.sing_up_btn);
        mLogInBtn = findViewById(R.id.log_in_btn);
        mGoToRegistrationBtn.setOnClickListener(this::onClick);
        mNextBtn.setOnClickListener(this::onClick);
        mLogInBtn.setOnClickListener(this::onClick);
    }

    private void checkIfEmailRegistered(){
        final String token = getToken.token;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.getavla.com/") // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<EmailValidate> call = service.checkIfEmailRegistered(token, mEmail);
        call.enqueue(new Callback<EmailValidate>() {
            @Override
            public void onResponse(Call<EmailValidate> call, Response<EmailValidate> response) {

                EmailValidate object = response.body();
                isExist = object.getPayload().isExists();
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
            public void onFailure(Call<EmailValidate> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void signUpWithEmail(String email, String password){
        final String token = getToken.token;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.getavla.com/") // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<EmailValidate> call = service.signUpWithEmail(token, email, password);
        call.enqueue(new Callback<EmailValidate>() {
            @Override
            public void onResponse(Call<EmailValidate> call, Response<EmailValidate> response) {
                EmailValidate object = response.body();
                Log.d(TAG, "onResponse: 222" + object.isOk());
                if(object.isOk()){
                    Toast.makeText(RegistrationActivity.this, "Email and pass added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistrationActivity.this, SignUp.class));
                }
            }

            @Override
            public void onFailure(Call<EmailValidate> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());

            }
        });
    }

    private void logInWithEmail(String email, String password){
        final String token = getToken.token;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.getavla.com/") // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<EmailValidate> call = service.loginWithEmail(token, email, password);
        call.enqueue(new Callback<EmailValidate>() {
            @Override
            public void onResponse(Call<EmailValidate> call, Response<EmailValidate> response) {
                EmailValidate object = response.body();
                PayloadModel payloadModel = object.getPayload();
                if(object.isOk()){
                    Toast.makeText(RegistrationActivity.this, "You logged in", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistrationActivity.this, ProfileActivity.class));
                } else {
                    Toast.makeText(RegistrationActivity.this, object.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EmailValidate> call, Throwable t) {
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
            checkIfEmailRegistered();
        }  else {
            emailED.requestFocus();
            emailED.setError("Invalid email");
        }
    }

    private class GetToken extends AsyncTask<Void, Void, String> {
        String token;

        @Override
        protected String doInBackground(Void... voids) {

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            String URL = "https://api.getavla.com/public_api/auth/create_token";
            JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    URL,
                    null,
                    response -> {
                        try {
                            JSONObject object = response.getJSONObject("payload");
                            object = response.getJSONObject("payload");
                            token = object.getString("token");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        System.out.println(error);
                        Log.e("response error", error.toString());
                    }
            );
            requestQueue.add(objectRequest);

            return token;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
