package com.avla.app.fragments.main;


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
import com.avla.app.adapter.TagAdapter;
import com.avla.app.model.ModelTag;
import com.avla.app.model.PayloadTag;
import com.avla.app.model.people.People;
import com.avla.app.model.people.PeopleModel;
import com.avla.app.model.people.PeoplePayload;

import java.util.ArrayList;
import java.util.List;

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
    private PeoplePayload peoplePayload;
    private ArrayList<String> tagList;
    private String query, token;
    EditText searchPeople;
    private String category = "all";
    private int offset = 0;
    private RecyclerView peopleRecycler, tagRecycler;
    private PeopleAdapter peopleAdapter;
    private ArrayList<People> peopleList = new ArrayList();
    private TagAdapter tagAdapter;
    private int limit = 25;
    private ArrayList<String> tagListName =  new ArrayList<>();
    private ArrayList<String> tagListId = new ArrayList<>();
    private Boolean isLoading = false;
    private Boolean isLoaded = false;

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
        token = getActivity().getIntent().getStringExtra("token");
        rxQueryPeople(limit);
        rxQueryTag();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        peopleRecycler = view.findViewById(R.id.people_recycler);
        peopleRecycler.setHasFixedSize(true);
        peopleRecycler.setLayoutManager(linearLayoutManager);
        tagRecycler = view.findViewById(R.id.tag_recycler);
        tagRecycler.setHasFixedSize(true);
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
                ArrayList<People> newPeopleList = new ArrayList<>();
                for (People man: peopleList){
                    if((man.getFirstName().toLowerCase()).contains(searchPeople.getText()) || (man.getLastName().toLowerCase()).contains(searchPeople.getText())){
                        Log.d(TAG, "afterTextChanged: " + man.getLastName());
                        newPeopleList.add(man);
                    }
                }
                peopleAdapter.updateList(newPeopleList);
            }
        });
        rxQueryPeople(limit);
        peopleRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                recyclerView.computeVerticalScrollRange();

                if (dy > 50 ) {
                    tagRecycler.setVisibility(View.GONE);
                } else if (dy < 50){
                    tagRecycler.setVisibility(View.VISIBLE);
                }
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                if (isLoading)
                    if ((visibleItemCount + firstVisibleItemPosition) < totalItemCount && firstVisibleItemPosition > 0) {
                        rxQueryPeople(limit += 25);
                        isLoading = false;
                    }

            }
        });
    }

    private void rxQueryPeople(int limit) {
        Observable.just((getPeople(category, tagList, offset, limit, query)))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
    private void rxQueryTag() {
        Observable.just(getTags())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    private Boolean getPeople(String category, ArrayList<String> tagList, int offset, int limit, String query) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();
        tagList = new ArrayList<>();
        IServer service = retrofit.create(IServer.class);
        Call<PeopleModel> call = service.getAllPeople(category, tagList, offset, limit, query, token);
        call.enqueue(new Callback<PeopleModel>() {
            @Override
            public void onResponse(Call<PeopleModel> call, Response<PeopleModel> response) {
                PeopleModel model = response.body();
                PeoplePayload payload = model.getPayload();
                List<People> people = payload.getPeople();
                if (peopleAdapter == null) {
                    peopleList = (ArrayList<People>) people;
                    peopleAdapter = new PeopleAdapter(getContext(), peopleList, token);
                    peopleRecycler.setAdapter(peopleAdapter);
                    isLoading = true;
                } else {
                    peopleAdapter.addAll((ArrayList<People>) people);
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
    private Boolean getTags(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<ModelTag> call = service.getAllTags(token);
        call.enqueue(new Callback<ModelTag>() {
            @Override
            public void onResponse(Call<ModelTag> call, Response<ModelTag> response) {
                ModelTag modelTag = response.body();
                List<PayloadTag> payload = modelTag.getPayload();
                for (int i = 0; i <payload.size(); i++) {
                    if (payload.get(i).getParentIds().size() == 0 )
                        tagListName.add(payload.get(i).getName());
                    tagListId.add(payload.get(i).getId());
                }
                tagAdapter = new TagAdapter(tagListName, tagListId);
                tagRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
                tagRecycler.setAdapter(tagAdapter);
            }

            @Override
            public void onFailure(Call<ModelTag> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });
        return true;
    }
}
