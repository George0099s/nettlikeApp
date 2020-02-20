package com.avla.app.view.main;


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

import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.adapter.PeopleAdapter;
import com.avla.app.model.people.People;
import com.avla.app.model.people.PeopleModel;
import com.avla.app.model.people.PeoplePayload;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends Fragment {
    private static final String TAG = "PeopleFragment";
    private ArrayList<String> tagList = new ArrayList<String>();
    private String query = "";
    private String token;
    private EditText searchPeople;
    private String category = "all";
    private int offset = 0;
    private RecyclerView peopleRecycler;
    private PeopleAdapter peopleAdapter;
    private ArrayList<People> peopleList = new ArrayList<>();
    private int limit = 25;
    private Boolean isLoading = false;

    public PeopleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_people, container, false);
        initViews(view);

        return view;
    }

    private void initViews(View view) {

        token = Objects.requireNonNull(getActivity()).getIntent().getStringExtra("token");
        rxQueryPeople(offset, query);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        peopleRecycler = view.findViewById(R.id.people_recycler);
        peopleRecycler.setHasFixedSize(true);
        peopleRecycler.setLayoutManager(linearLayoutManager);
        searchPeople = view.findViewById(R.id.search_people);
        searchPeople.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getPeople(category, tagList, offset, limit, searchPeople.getText().toString());
            }
        });
        rxQueryPeople(offset, query);
        peopleRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                if (isLoading)
                    if ((visibleItemCount + firstVisibleItemPosition) < totalItemCount && firstVisibleItemPosition > 0) {
                        if (peopleList.size() != 0) {
                            rxQueryPeople(offset += 25, query);
                            isLoading = false;
                        }
                    }

            }
        });
    }

    private void rxQueryPeople(int offset, String query) {
        Observable.just((getPeople(category, tagList, offset, limit, query)))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
//    private void rxQueryTag() {
//        Observable.just(getTags())
//                .subscribeOn(Schedulers.io())
//                .subscribe();
//    }

    private Boolean getPeople(String category, ArrayList<String> tagList, int offset, int limit, String query) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();
        IServer service = retrofit.create(IServer.class);
        Call<PeopleModel> call = service.getAllPeople(category, tagList, offset, limit, query, token);
        call.enqueue(new Callback<PeopleModel>() {
            @Override
            public void onResponse(Call<PeopleModel> call, Response<PeopleModel> response) {
                PeopleModel model = response.body();
                assert model != null;
                PeoplePayload payload = model.getPayload();
                List<People> people = payload.getPeople();
                if(query.equals("") || query.isEmpty()){
                    if (people != null)
                    if (peopleAdapter == null) {
                        peopleList = (ArrayList<People>) people;
                        peopleAdapter = new PeopleAdapter(getContext(), peopleList, token);
                        peopleRecycler.setAdapter(peopleAdapter);
                        isLoading = true;
                    } else {
                        peopleAdapter.addAll((ArrayList<People>) people);
                        isLoading = true;
                    }
                } else {
                    peopleList = (ArrayList<People>) people;
                    peopleAdapter.updateList(peopleList);
                    isLoading = true;
                }
            }
            @Override
            public void onFailure(Call<PeopleModel> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });
        return true;
    }
}