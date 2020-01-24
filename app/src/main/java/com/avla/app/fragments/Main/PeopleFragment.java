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
import com.avla.app.model.People;
import com.avla.app.model.PeopleModel;
import com.avla.app.model.PeoplePayload;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends Fragment {
    private static final String TAG = "PeopleFragment";
    private PeoplePayload peoplePayload;
    private ArrayList<String> tagList;
    private String query ;
    private static final Scheduler THREAD_2 = Schedulers.newThread();
    private RecyclerView peoplerecycler;
    private PeopleAdapter peopleAdapter;
    private ArrayList<People> peopleList = new ArrayList();
    private int limit = 25;
    private Boolean isLoading;
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

        Observable.just((getPeople(String.valueOf(limit))))
                .subscribeOn(Schedulers.io())
                .observeOn(THREAD_2)
                .subscribe();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        peoplerecycler = view.findViewById(R.id.people_recycler);
        peoplerecycler.setLayoutManager(linearLayoutManager);
        peoplerecycler.addOnScrollListener(new RecyclerView.OnScrollListener()
        {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                isLoading = true;

                    if ((visibleItemCount + firstVisibleItemPosition) < totalItemCount && firstVisibleItemPosition > 0)
                    {
                        isLoading = true;

                        limit+=25;

                        getPeople(String.valueOf(limit));
                    }

            }
        });
    }

    private Boolean  getPeople(String limit){
        String token = getActivity().getIntent().getStringExtra("token");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();
        tagList = new ArrayList<>();
        IServer service = retrofit.create(IServer.class);
        Call<PeopleModel> call = service.getAllPeople("all", tagList,"0", limit, query, token);
        call.enqueue(new Callback<PeopleModel>() {
            @Override
            public void onResponse(Call<PeopleModel> call, Response<PeopleModel> response) {
                PeopleModel model = response.body();
                PeoplePayload payload = model.getPayload();
                List<People> people = payload.getPeople();
                peopleList = (ArrayList<People>) people;
                peopleAdapter = new PeopleAdapter(getContext(),peopleList);
                peoplerecycler.setAdapter(peopleAdapter);

            }

            @Override
            public void onFailure(Call<PeopleModel> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });
        return true;
    }

}
