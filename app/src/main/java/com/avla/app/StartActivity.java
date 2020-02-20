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
import com.avla.app.Interface.IServer;
import com.avla.app.authorization.RegistrationActivity;
import com.avla.app.data.AppDatabase;
import com.avla.app.data.TokenDao;
import com.avla.app.data.TokenEntity;
import com.avla.app.model.Payload;
import com.avla.app.model.UserModel;
import com.avla.app.model.UserSingleton;
import com.google.firebase.iid.FirebaseInstanceId;

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
    private String sende_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mPrefs = getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);
        db =  Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "avla_DB")
                .allowMainThreadQueries()
                .build();
        tokenDao = db.tokenDao();
        getToken(tokenDao);
//        startService(new Intent(this, PushService.class));

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
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<Payload> call = service.validateToken(token2);
        call.enqueue(new Callback<Payload>() {
            @Override
            public void onResponse(Call<Payload> call, Response<Payload> response) {
                Payload object = response.body();
//                Intent intent = new Intent(StartActivity.this, RegistrationActivity.class);
//                intent.putExtra("token", token2);
//                startActivity(intent);
                if (object != null){
                if(object.getOk()){
                    if (mPrefs.getBoolean("firstrun", true)) {
                        Intent intent = new Intent(StartActivity.this, OnBoarding.class);
                        intent.putExtra("token", token2);
                        startActivity(intent);
                        mPrefs.edit().putBoolean("firstrun", false).apply();
                    } else {
                        if(mPrefs.getBoolean("isLogIn", false)) {
                            Intent intent = new Intent(StartActivity.this, MainActivity.class);
                            UserSingleton.INSTANCE.setExist(true);
                            intent.putExtra("token", token2);
                            sendId(token2);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(StartActivity.this, RegistrationActivity.class);
                            intent.putExtra("token", token2);
                            startActivity(intent);
                        }
                    }
                } else {
                    Toast.makeText(StartActivity.this, "Wrong token", Toast.LENGTH_SHORT).show();
                    tokenDao.deleteAll();
                    getToken(tokenDao);
                }
                } else {
                    Toast.makeText(StartActivity.this, "There are some problems with server "+"\n" + " please try later", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Payload> call, Throwable t) {
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
                            UserModel userModel = response.body();
                            Log.d("123", "onResponse: " + userModel.getOk());

                            Log.d("123", "onResponse:key " + key);
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            Log.d(TAG, "onResponse: signUp fail " + t.getMessage());

                        }
                    });
                });




    }
    private class GetToken extends AsyncTask<Void, Void, String> {
        String token;
        @Override
        protected String doInBackground(Void... voids) {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            String URL = Constants.BASIC_URL+"public_api/auth/create_token";
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