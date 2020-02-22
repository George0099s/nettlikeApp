package com.nettlike.app.view.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nettlike.app.Constants;
import com.nettlike.app.Interface.IServer;
import com.nettlike.app.R;
import com.nettlike.app.adapter.ActivityAdapter;
import com.nettlike.app.model.activity.Model;
import com.nettlike.app.model.activity.Payload;
import com.nettlike.app.model.activity.Target;

import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ActivitiesFragment extends Fragment {
    private static final String TAG = "ActivitiesFragment";
    private ActivityAdapter activityAdapter;
    private List<Target> targets;
    private RecyclerView activityRecycler;
    private String token;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_activities, container, false);
            initView(view);
        return view;
    }
    private void initView(View view) {
        token = getActivity().getIntent().getStringExtra("token");
        activityRecycler = view.findViewById(R.id.activity_recycler);
        activityRecycler.setHasFixedSize(true);
        activityRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        Observable.fromCallable(new CallableGetActivity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
    private Void getActivities(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<Model> call = service.getActivity(token, 0 ,25);
        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                Model object = response.body();
                List<Payload> payload = object.getPayload();
                activityAdapter = new ActivityAdapter(payload, getActivity(), token);
                activityRecycler.setAdapter(activityAdapter);
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());

            }
        });
        return null;
    }

    private class CallableGetActivity implements Callable<Void> {

        @Override
        public Void call() throws Exception {
            return getActivities();
        }
    }
}
