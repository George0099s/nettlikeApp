package com.avla.app.Authorization;

import androidx.annotation.NonNull;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

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
    private GetToken task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        task = new GetToken();
        task.execute();

        mEmailED = findViewById(R.id.email_ed);
        mPasswordED = findViewById(R.id.password_ed);
        mNextBtn = findViewById(R.id.next_btn);
        mNextBtn.setOnClickListener(this::onClick);
    }



    private void onClick(View v){
        mEmail = mEmailED.getText().toString();

        ValidateEmail validateEmail = new ValidateEmail();
        validateEmail.doInBackground(mEmail);
        validateEmail.execute();
//        Functions.checkEmail(mEmailED, mPasswordED);


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

    private class ValidateEmail extends AsyncTask<String, Void, String> {
        String token = task.token;

        @Override
        protected String doInBackground(String... email) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.getavla.com/") // Адрес сервера
                    .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                    .build();

            Server service = retrofit.create(Server.class);
            Call<EmailValidate> call = service.addEmail(token, mEmail);
            Log.d(TAG, "doInBackground: 222" + mEmail);
            call.enqueue(new Callback<EmailValidate>() {
                @Override
                public void onResponse(Call<EmailValidate> call, Response<EmailValidate> response) {

                    EmailValidate object = response.body();
                    PayloadModel payloadModel = object.getPayload();

                    Log.d(TAG, "onResponse: 123" + payloadModel.isExists());
                }

                @Override
                public void onFailure(Call<EmailValidate> call, Throwable t) {
                    Log.d(TAG, "onResponse: onfailure " + t.getMessage());

                }
            });

            return "123";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

//    private class ValidateEmail extends AsyncTask<String, Void, String> {
//        String token = task.token;
//        @Override
//        protected String doInBackground(String... email) {
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl("https://api.getavla.com/") // Адрес сервера
//                    .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
//                    .build();
//
//            Server service = retrofit.create(Server.class);
//            Call<JSONObject> addEmail2 = service.addEmail2(token, mEmail);
//            addEmail2.enqueue(new Callback<JSONObject>() {
//
//                @Override
//                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
//
//                    JSONObject object = response.body();
//                    Log.d(TAG, "onResponse: 123" + object.toString());
//                }
//
//                @Override
//                public void onFailure(Call<JSONObject> call, Throwable t) {
//                    Log.d(TAG, "onResponse: onfailure " + t.getMessage());
//                }
//            });
//
//            return "123";
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//        }
//    }



}
