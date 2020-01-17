package com.avla.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.avla.app.Authorization.SignUp;
import com.avla.app.Database.AppDatabase;
import com.avla.app.Entity.TokenEntity;
import com.avla.app.Interface.TokenDao;

import org.json.JSONException;
import org.json.JSONObject;

public class StartActivity extends AppCompatActivity {
    private SharedPreferences mPrefs;
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

        if(tokenDao.getAll() == null){
            GetToken getToken = new GetToken();
            getToken.execute();

        }
        startActivity(new Intent(StartActivity.this, SignUp.class));


        //        if (mPrefs.getBoolean("firstrun", true)) {
//            startActivity(new Intent(StartActivity.this, OnBoarding.class));
//            mPrefs.edit().putBoolean("firstrun", false).commit();
//        } else {
//            startActivity(new Intent(StartActivity.this, RegistrationActivity.class));
//        }

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
                            TokenEntity tokenEntity = new TokenEntity();
                            db =  Room.databaseBuilder(getApplicationContext(),
                                    AppDatabase.class, "avlaDB")
                                    .allowMainThreadQueries()
                                    .build();
                            tokenDao = db.tokenDao();
                            tokenEntity.setToken(token);
//                            Log.d(TAG, "onCreate: "+ tokenEntity);
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
