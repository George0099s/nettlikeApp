package com.avla.app.Authorization;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.avla.app.Interface.Server;
import com.avla.app.Model.EmailValidate;
import com.avla.app.Model.PayloadModel;
import com.avla.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
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
    private String mEmail, mPass;
    private Button mNextBtn;
    private GetToken getToken;
    private Boolean isExist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initViews();
        getToken();

    }

    private void getToken() {
        getToken = new GetToken();
        getToken.execute();

    }

    private void initViews() {
        mEmailED = findViewById(R.id.email_ed);
        mPasswordED = findViewById(R.id.password_ed);
        mNextBtn = findViewById(R.id.next_btn);
        mNextBtn.setOnClickListener(this::onClick);
    }

    private Boolean checkIfEmailRegistered(){
        final String token = getToken.token;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.getavla.com/") // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        Server service = retrofit.create(Server.class);
        Call<EmailValidate> call = service.checkIfEmailRegistered(token, mEmail);
        call.enqueue(new Callback<EmailValidate>() {
            @Override
            public void onResponse(Call<EmailValidate> call, Response<EmailValidate> response) {

                EmailValidate object = response.body();
                isExist = object.getPayload().isExists();
                notifyAboutNewItems();
                Log.d(TAG, "onResponse: 123 " + isExist);
            }

            @Override
            public void onFailure(Call<EmailValidate> call, Throwable t) {
                Log.d(TAG, "onResponse: onfailure " + t.getMessage());

            }
        });
        return  isExist;
    }
    private void onClick(View v){
        mEmail = mEmailED.getText().toString();

        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        CharSequence inputStr = mEmailED.getText();

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            checkIfEmailRegistered();
//            CheckIfEmailRegistered checkIfEmailRegistered = new CheckIfEmailRegistered();
//            checkIfEmailRegistered.doInBackground(mEmail);
//            checkIfEmailRegistered.execute();
//
//            try {
//                Log.d(TAG, "onClick: isExist " + checkIfEmailRegistered.get());
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            if(isExist){
//                mEmailED.requestFocus();
//                mEmailED.setError("Email has already exist");
//            } else {
//                mPasswordED.setVisibility(View.VISIBLE);
//            }
        }  else {
            mEmailED.requestFocus();
            mEmailED.setError("Invalid email");
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
                            Log.d(TAG, "onResponse: " + object.getString("token"));
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

//    private class CheckIfEmailRegistered extends AsyncTask<String, Void, Boolean> {
//        String token = getToken.token;
//        PayloadModel payload = new PayloadModel();
//
//        @Override
//        protected Boolean doInBackground(String... email) {
//            Boolean ex;
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl("https://api.getavla.com/") // Адрес сервера
//                    .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
//                    .build();
//
//            Server service = retrofit.create(Server.class);
//            Call<EmailValidate> call = service.checkIfEmailRegistered(token, mEmail);
//            call.enqueue(new Callback<EmailValidate>() {
//                @Override
//                public void onResponse(Call<EmailValidate> call, Response<EmailValidate> response) {
//
//                    EmailValidate object = response.body();
//                    payload = object.getPayload();
//                    Log.d(TAG, "onResponse: 123 " + payload.isExists());
//                }
//
//                @Override
//                public void onFailure(Call<EmailValidate> call, Throwable t) {
//                    Log.d(TAG, "onResponse: onfailure " + t.getMessage());
//
//                }
//            });
//            return payload.isExists();
//        }
//
//        @Override
//        protected void onPostExecute(Boolean exist) {
//            Log.d(TAG, "onPostExecute: ex" + exist);
//
//            super.onPostExecute(exist);
//        }
//    }
}
