package com.nettlike.app.view.main.profileInnerFragments;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nettlike.app.Constants;
import com.nettlike.app.Interface.IServer;
import com.avla.app.R;
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

public class UserActivitiesActivity extends AppCompatActivity {

    private static final String TAG = "UserActivitiesActivity";
    private ActivityAdapter activityAdapter;
    private List<Target> targets;
    private RecyclerView activityRecycler;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
    }

    private void initView() {
        token = getIntent().getStringExtra("token");
        activityRecycler = findViewById(R.id.activity_recycler);
        activityRecycler.setHasFixedSize(true);
        activityRecycler.setLayoutManager(new LinearLayoutManager(this));
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
                activityAdapter = new ActivityAdapter(payload, UserActivitiesActivity.this, token);
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
