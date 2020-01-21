package com.avla.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.avla.app.Authorization.RegistrationActivity;
import com.avla.app.Database.AppDatabase;
import com.avla.app.Entity.TokenEntity;
import com.avla.app.Interface.IServer;
import com.avla.app.Interface.TokenDao;
import com.avla.app.Model.Payload;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StartActivity extends AppCompatActivity {
    private SharedPreferences mPrefs;
    private String token;
    private static final String TAG = "StartActivity";
    private AppDatabase db;
    private TokenDao tokenDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mPrefs = getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);
        db =  Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "avlaDB")
                .allowMainThreadQueries()
                .build();

        tokenDao = db.tokenDao();
        getToken(tokenDao);

//        startActivity(new Intent(StartActivity.this, OnBoarding.class));


    }

    private void getToken(TokenDao tokenDao){
        if(tokenDao.getAll().size() == 0){
            GetToken getToken = new GetToken();
            getToken.execute();
        } else {
            validateToken(tokenDao.getToken().get(0).toString());
        }

    }

    private void validateToken(String token2) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.getavla.com/") // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<Payload> call = service.validateToken(token2);
        call.enqueue(new Callback<Payload>() {
            @Override
            public void onResponse(Call<Payload> call, Response<Payload> response) {
                Payload object = response.body();

                if(object.getOk() == true){
                    if (mPrefs.getBoolean("firstrun", true)) {
                        Intent intent = new Intent(StartActivity.this, OnBoarding.class);
                        intent.putExtra("token", token2);
                        startActivity(intent);
                        mPrefs.edit().putBoolean("firstrun", false).commit();
                    } else {
                        Intent intent = new Intent(StartActivity.this, RegistrationActivity.class);
                        intent.putExtra("token", token2);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(StartActivity.this, "Wrong token", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Payload> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());

            }
        });
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
                            validateToken(token);
                            TokenEntity tokenEntity = new TokenEntity();
                            db =  Room.databaseBuilder(getApplicationContext(),
                                    AppDatabase.class, "avlaDB")
                                    .allowMainThreadQueries()
                                    .build();
                            tokenEntity.setToken(token);
                            tokenDao.insert(tokenEntity);
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

        @Override
        protected void onPostExecute(String s) {

        }
    }
}
