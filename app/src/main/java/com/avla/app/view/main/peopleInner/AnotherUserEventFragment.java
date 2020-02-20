package com.avla.app.view.main.peopleInner;

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
import com.avla.app.adapter.EventAdapter;
import com.avla.app.model.Account;
import com.avla.app.model.Event;
import com.avla.app.model.Model;
import com.avla.app.model.Payload;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AnotherUserEventFragment extends Fragment {
    private static final String TAG = "AnotherUserEventFragmen";
    private RecyclerView eventRecyclerView;
    private EventAdapter eventAdapter;
    private String token, currentUserId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_another_user_event, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        token = getActivity().getIntent().getStringExtra("token");
        eventRecyclerView= view.findViewById(R.id.another_user_event);
        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        currentUserId = getActivity().getIntent().getStringExtra("user id");
        Observable.just((getUserEvent()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private boolean getUserEvent() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<Model> call = service.getAnotherUserInfo(currentUserId,token);
        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                Model model= response.body();
                Payload payload = model.getPayload();
                Account account = payload.getAccount();
                List<Event> events = account.getEvents();

                eventAdapter = new EventAdapter(events, getContext(), token);
                eventRecyclerView.setAdapter(eventAdapter);
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

        return true;
    }

}
