package com.avla.app.fragments.main;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.AvlaApplication;
import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.adapter.DialogAdapter;
import com.avla.app.model.dialog.DiaModel;
import com.avla.app.model.dialog.DiaPayload;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
public class MessageFragment extends Fragment {

    private static final String TAG = "MessageFragment";
    private RecyclerView diaRecycler;
    private DialogAdapter dialogAdapter;
    private List<DiaPayload> dialogsList;
    private String token;
    private Socket socket;

    public MessageFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new MessageFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AvlaApplication app = (AvlaApplication) getActivity().getApplication();
//        socket = app.getManager();
//        socket.on("new_dialog", onGetNewDia);
//        socket.on("new message", onNewMessage);
//        socket.connect();
    }
    private Emitter.Listener onNewMessage = args -> getActivity().runOnUiThread(() -> {
        JSONObject data = (JSONObject) args[0];
        String username;
        String message;
        try {

            Log.d(TAG, "run:  " + data.getString("message"));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return;
        }

    });
    private void initView(View view) {


        diaRecycler = view.findViewById(R.id.dialogs_recycler);
        diaRecycler.setHasFixedSize(true);
        diaRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        dialogsList = new ArrayList<>();
        token = getActivity().getIntent().getStringExtra("token");
        Observable.just((getAllDia()))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
    private Emitter.Listener onGetNewDia = args -> getActivity().runOnUiThread(() -> {
        JSONObject data = (JSONObject) args[0];
        try {
            JSONObject payload = data.getJSONObject("payload");
            JSONArray members = payload.getJSONArray("members");
            Log.d(TAG, ":data payload" + data.getJSONObject("payload").toString());
            Log.d(TAG, ":data members" + members.length());
        } catch (JSONException e) {
            Log.d(TAG, ": " + e.getMessage());
        }
    });




    private Boolean getAllDia(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<DiaModel> call = service.getAllDialogs(token);
        call.enqueue(new Callback<DiaModel>() {
            @Override
            public void onResponse(Call<DiaModel> call, Response<DiaModel> response) {
                Log.d(TAG, "onResponse: " + call.request().url());
                DiaModel model = response.body();
                Log.d(TAG, "onResponse: " + response);
                dialogsList = model.getPayloadList();
                Log.d(TAG, "onResponse: size" + dialogsList.size());
                dialogAdapter = new DialogAdapter(dialogsList, getContext(), token);
                diaRecycler.setAdapter(dialogAdapter);
            }

            @Override
            public void onFailure(Call<DiaModel> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return  null;
    }
}
