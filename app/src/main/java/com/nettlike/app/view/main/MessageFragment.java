package com.nettlike.app.view.main;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.R;
import com.github.nkzawa.emitter.Emitter;
import com.nettlike.app.Constants;
import com.nettlike.app.Interface.IServer;
import com.nettlike.app.adapter.DialogAdapter;
import com.nettlike.app.adapter.SwipeToDeleteCallback;
import com.nettlike.app.model.LastMessage;
import com.nettlike.app.model.UserSingleton;
import com.nettlike.app.model.dialog.DiaMember;
import com.nettlike.app.model.dialog.DiaModel;
import com.nettlike.app.model.dialog.DiaPayload;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
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

    public MessageFragment() {
        // Required empty public constructor
    }

//    public static Fragment newInstance() {
//        return new MessageFragment();
//    }


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

    }

    private void initView(View view) {
//        NettlikeApplication app = (NettlikeApplication) Objects.requireNonNull(getActivity()).getApplication();
//        Manager manager;
//        manager = app.getManager();
//        Socket socket = manager.socket("/all");
//        socket.on("new_dialog", onGetNewDia);
//        socket.on("new_message", onNewMessage);
//        socket.on("typing_start", onTypingStart);
//        socket.on("typing_stop", onTypingStop);
//        socket.connect();
        diaRecycler = view.findViewById(R.id.dialogs_recycler);
        diaRecycler.setHasFixedSize(true);
        diaRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        dialogsList = new ArrayList<>();
        token = getActivity().getIntent().getStringExtra("token");
        enableSwipeToDeleteAndUndo();
        Observable.just((getAllDia()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public void onResume() {
        super.onResume();
        Observable.just((getAllDia()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private Emitter.Listener onNewMessage = args -> Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
        String dialogId, createBy, user;
        int unreadCount;
        LastMessage lastMsg = new LastMessage();
        List<DiaMember> members = new ArrayList<>();
        DiaMember userMember = new DiaMember();
        DiaMember otherMember = new DiaMember();
        try {
            JSONArray jsonArray = new JSONArray(Arrays.toString(args));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            Log.d("abc", ": 123 " + jsonObject);
            createBy = jsonObject.getString("created_by");
            dialogId = jsonObject.getString("dialog_id");
            user = UserSingleton.INSTANCE.getUserId();
            userMember.setId(user);
            otherMember.setId(createBy);
            members.add(userMember);
            members.add(otherMember);

            lastMsg.setCreatedBy(createBy);
            lastMsg.setText(jsonObject.getString("text"));
            lastMsg.setDialogId(dialogId);
            DiaPayload dialog = new DiaPayload();

            dialog.setMembers(members);
            dialog.setId(dialogId);
            dialog.setLastMessage(lastMsg);
//            dialog.setUnreadCount(unreadCount);

            dialogAdapter.update(dialog);

        } catch (JSONException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));

        }

    });

    private Emitter.Listener onTypingStart = args -> Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
        String diaId;
        try {
            JSONArray jsonArray = new JSONArray(Arrays.toString(args));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            diaId = jsonObject.getString("dialog_id");
            dialogAdapter.isTyping(true, diaId);

        } catch (JSONException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }

    });

    private Emitter.Listener onTypingStop = args -> Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
        String diaId;
        try {
            JSONArray jsonArray = new JSONArray(Arrays.toString(args));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            diaId = jsonObject.getString("dialog_id");
            dialogAdapter.isTyping(false, diaId);
            Log.d(TAG, ": stop " + jsonObject);
        } catch (JSONException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }

    });

    private Emitter.Listener onGetNewDia = args -> Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
       String diaId, lastMessage;
       List<Member> members;
        try {
            JSONArray jsonArray = new JSONArray(Arrays.toString(args));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            Log.d("abcd", ": dia " + jsonArray);
            DiaPayload dialog = new DiaPayload();
            dialogAdapter.update(dialog);
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
            public void onResponse(@NonNull Call<DiaModel> call, @NonNull Response<DiaModel> response) {
                DiaModel model = response.body();
                dialogsList = Objects.requireNonNull(model).getPayloadList();
                dialogAdapter = new DialogAdapter(dialogsList, getContext(), token);
                diaRecycler.setAdapter(dialogAdapter);
            }
            @Override
            public void onFailure(@NonNull Call<DiaModel> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return  null;
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final DiaPayload item = dialogAdapter.getData().get(position);
                dialogAdapter.removeItem(position, item.getId());
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(diaRecycler);
    }

}
