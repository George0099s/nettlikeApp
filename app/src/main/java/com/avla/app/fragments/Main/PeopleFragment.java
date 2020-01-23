package com.avla.app.fragments.Main;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.adapter.PeopleAdapter;
import com.avla.app.model.PeoplePojo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends Fragment {
    private static final String TAG = "PeopleFragment";
    private PeoplePojo peoplePojo;
    private ArrayList<String> tagList;
    private String query = null;
    private RecyclerView peoplerecycler;
    private PeopleAdapter peopleAdapter;
    private ArrayList<PeoplePojo> peopleList = new ArrayList();

    public PeopleFragment() {
        // Required empty public constructor
    }
    public static Fragment newInstance() {
        return new PeopleFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_people, container, false);
        initViews(view);

        return view;
    }

    private void initViews(View view) {
        peoplerecycler = view.findViewById(R.id.people_recycler);
        getPeople();
        peoplerecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void  getPeople(){
        String token = getActivity().getIntent().getStringExtra("token");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();
        tagList = new ArrayList<>();
        IServer service = retrofit.create(IServer.class);
        Call<PeoplePojo> call = service.getAllPeople(token,"all", tagList,0, 25, query);
        call.enqueue(new Callback<PeoplePojo>() {
            @Override
            public void onResponse(Call<PeoplePojo> call, Response<PeoplePojo> response) {
                PeoplePojo peoplePojo = response.body();
                Log.d(TAG, "onResponse: " + response.body());

//                peopleList.add(peoplePojo);
//                peopleAdapter = new PeopleAdapter(getContext(),peopleList);
//                peoplerecycler.setAdapter(peopleAdapter);

            }

            @Override
            public void onFailure(Call<PeoplePojo> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });
    }

}
