package com.avla.app.view.main.postInner;

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
import com.avla.app.model.Event;
import com.avla.app.model.EventPayload;
import com.avla.app.model.ModelEvent;

import org.json.JSONArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventFragment extends Fragment {
    private static final String TAG = "EventFragment";
    private RecyclerView eventRecycler;
    private EventAdapter eventAdapter;
    private String token, query;
    private JSONArray tags;
    private int offset, limit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        query = null;
        offset = 0;
        limit = 25;
        token = getActivity().getIntent().getStringExtra("token");
        tags = new JSONArray();
        eventRecycler = view.findViewById(R.id.event_recycler);
        eventRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        eventRecycler.setHasFixedSize(true);
        getEvent();
    }

    private Boolean getEvent(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<ModelEvent> call = service.getEvent(token, tags, offset, query, limit);
        call.enqueue(new Callback<ModelEvent>() {

            @Override
            public void onResponse(Call<ModelEvent> call, Response<ModelEvent> response) {
                ModelEvent modelEvent = response.body();
                EventPayload eventPayload = modelEvent.getPayload();
                List<Event> events = eventPayload.getEvents();
                eventAdapter = new EventAdapter(events, getContext(), token);
                eventRecycler.setAdapter(eventAdapter);
            }

            @Override
            public void onFailure(Call<ModelEvent> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.printStackTrace();
            }
        });
        return true;
    }
}
