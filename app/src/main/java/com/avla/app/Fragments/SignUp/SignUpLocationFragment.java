package com.avla.app.Fragments.SignUp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.avla.app.Adapter.LocationCountryAdapter;
import com.avla.app.Constants;
import com.avla.app.Database.AppDatabase;
import com.avla.app.Entity.TokenEntity;
import com.avla.app.Interface.IServer;
import com.avla.app.Interface.TokenDao;
import com.avla.app.Model.ModelLocation;
import com.avla.app.Model.Payload;
import com.avla.app.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpLocationFragment extends Fragment {
    private static final String TAG = "SignUpLocationFragment";
    public static final String MyPREFERENCES = Constants.MY_PREFERENCES ;
    private SharedPreferences sharedPreferences;
    List<TokenEntity> tokenList ;
    private String[] countries;
    private RecyclerView recyclerViewLocatoin;
    private LocationCountryAdapter locationCountryAdapter;
    private ArrayList<String> countryListName;
    private ArrayList<String> countryListId;
    private ArrayList<String> cityList;
    private String token;
    private Context mContext;
    EditText searchCountry;
    public SignUpLocationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_location, container, false);
        initViews(view);
        getAllCountries(token);
        return view;
    }

    private void initViews(View view) {
        AppDatabase db = Room.databaseBuilder(getContext(),
                AppDatabase.class, "avlaDB")
                .allowMainThreadQueries()
                .build();
        TokenDao tokenDao = db.tokenDao();

        countryListName = new ArrayList<>();
        cityList = new ArrayList<>();
        countryListId = new ArrayList<>();


        mContext = getContext();

        locationCountryAdapter = new LocationCountryAdapter(countryListName, countryListId, cityList, mContext, sharedPreferences);
        tokenList = tokenDao.getToken();
        token = String.valueOf(tokenList.get(0));
        recyclerViewLocatoin = view.findViewById(R.id.location_recycler);
        recyclerViewLocatoin.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewLocatoin.setAdapter(locationCountryAdapter);
        searchCountry = view.findViewById(R.id.search_country);
        searchCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                List<String> newCountryList = new ArrayList<>();
                for (String country: countryListName){
                    if(country.toLowerCase().contains(searchCountry.getText())){
                        newCountryList.add(country);
                    }
                }
                locationCountryAdapter.updateList(newCountryList);
            }
        });
    }


    private void getAllCountries(String token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.getavla.com/") // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<ModelLocation> call = service.getAllCountries(token);
        call.enqueue(new Callback<ModelLocation>() {
            @Override
            public void onResponse(Call<ModelLocation> call, Response<ModelLocation> response) {

                ModelLocation list = response.body();
                List<Payload> payload = list.getPayload();
                for (int i = 0; i < payload.size(); i++) {
                    countryListName.add(payload.get(i).getName());
                    countryListId.add(payload.get(i).getId());
                }

            }

            @Override
            public void onFailure(Call<ModelLocation> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
