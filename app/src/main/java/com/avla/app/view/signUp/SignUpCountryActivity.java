package com.avla.app.view.signUp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.adapter.LocationCountryAdapter;
import com.avla.app.model.ModelLocation;
import com.avla.app.model.Payload;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignUpCountryActivity extends AppCompatActivity {
    private static final String TAG = "SignUpCountryActivity";
    public static final String MyPREFERENCES = Constants.MY_PREFERENCES ;
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerViewLocatoin;
    private LocationCountryAdapter locationCountryAdapter;
    private ArrayList<Payload> countryListName;
    private ArrayList<String> countryListId;
    private ArrayList<String> cityList;
    private String token;
    private Context mContext;
    EditText searchCountry;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_country);
        initViews();
        Observable.just(( getAllCountries(token)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }
    private void initViews() {
        activity = this;
        countryListName = new ArrayList<>();
        cityList = new ArrayList<>();
        countryListId = new ArrayList<>();
        mContext = this;
        token = getIntent().getStringExtra("token");
        recyclerViewLocatoin = findViewById(R.id.location_recycler);
        recyclerViewLocatoin.setLayoutManager(new LinearLayoutManager(this));
        searchCountry = findViewById(R.id.search_country);
        searchCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                List<Payload> newCountryList = new ArrayList<>();
                List<String> newCountryIdList = new ArrayList<>();
                for (Payload country: countryListName){
                    if(country.getName().toLowerCase().contains(searchCountry.getText())){
                        newCountryList.add(country);
                    }
                }
                locationCountryAdapter.updateList(newCountryList, newCountryIdList);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1){
            Log.d(TAG, "onActivityResult: " + data.getData());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private Boolean getAllCountries(String token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<ModelLocation> call = service.getAllCountries(token);
        call.enqueue(new Callback<ModelLocation>() {
            @Override
            public void onResponse(Call<ModelLocation> call, Response<ModelLocation> response) {
                Log.d(TAG, "onResponse: " + call.request().url());
                ModelLocation list = response.body();
                List<Payload> payload = list.getPayload();
                for (int i = 0; i < payload.size(); i++) {
                    countryListName.add(payload.get(i));
                    countryListId.add(payload.get(i).getId());
                }
                locationCountryAdapter = new LocationCountryAdapter(countryListName, countryListId, cityList, mContext, activity);
                recyclerViewLocatoin.setAdapter(locationCountryAdapter);
            }

            @Override
            public void onFailure(Call<ModelLocation> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });
        return true;
    }
}
