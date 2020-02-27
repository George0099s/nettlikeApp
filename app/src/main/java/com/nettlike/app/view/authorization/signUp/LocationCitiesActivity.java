package com.nettlike.app.view.authorization.signUp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nettlike.app.Constants;
import com.nettlike.app.Interface.IServer;
import com.nettlike.app.R;
import com.nettlike.app.adapter.LocationCityAdapter;
import com.nettlike.app.model.ModelLocation;
import com.nettlike.app.model.Payload;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationCitiesActivity extends AppCompatActivity  {
    private static final String TAG = "LocationCitiesActivity";
    private LocationCityAdapter locationCityAdapter;
    private RecyclerView locationRecycerView;
    private Payload country;
    private ArrayList<String> citiesList;
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private EditText searchCity;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_cities);
        initViews();
    }



    private void initViews() {
        activity = this;
        mContext = this;
        country = getIntent().getParcelableExtra("country");
        sharedPreferences = getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);
        locationRecycerView = findViewById(R.id.location_recycler);
        searchCity = findViewById(R.id.search__city);
        searchCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                List<String> newCityList = new ArrayList<>();
                for (String country: citiesList){
                    if(country.toLowerCase().contains(searchCity.getText())){
                        newCityList.add(country);
                    }
                }
                locationCityAdapter.updateList(newCityList);
            }
        });
        getAllCities(getIntent().getStringExtra("token"),country.getId());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void getAllCities(String token, String countryId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<ModelLocation> call = service.getAllCities(countryId, token);
        call.enqueue(new Callback<ModelLocation>() {


            @Override
            public void onResponse(Call<ModelLocation> call, Response<ModelLocation> response) {
                citiesList = new ArrayList<>();
                ModelLocation list = response.body();
                List<Payload> payload = list.getPayload();
                for (int i = 0; i < payload.size(); i++) {
                    citiesList.add(payload.get(i).getName());
                }
                locationRecycerView.setLayoutManager(new LinearLayoutManager(mContext));
                locationCityAdapter = new LocationCityAdapter (citiesList, mContext, sharedPreferences, activity);
                locationRecycerView.setAdapter(locationCityAdapter);
            }

            @Override
            public void onFailure(Call<ModelLocation> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());

            }
        });
    }
}
