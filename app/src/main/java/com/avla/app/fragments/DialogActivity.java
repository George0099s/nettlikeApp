package com.avla.app.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.AvlaApplication;
import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.adapter.MessageAdapter;
import com.avla.app.model.Message;
import com.avla.app.model.MessageModel;
import com.avla.app.model.MessagePayload;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DialogActivity extends AppCompatActivity {
    private static final String TAG = "DialogActivity";
    private JSONObject tokenJson = new JSONObject();
    private String token, dialogId;
    private EditText messageBody;
    private ImageView sendImg;
    private RecyclerView messageRecycler;
    private MessageAdapter messageAdapter;
    private Socket mSocket;
    private Manager manager;
    private List<Message> mMessages = new ArrayList<Message>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        initView();

    }

    private void initView() {
        token = getIntent().getStringExtra("token");
        try {
            tokenJson.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AvlaApplication app = (AvlaApplication) getApplication();
        manager = app.getManager();
        mSocket = manager.socket("/all");

        mSocket.emit("create_connection", tokenJson);
        mSocket.on("create_connection_successful", onConnectionSuccessful);
        mSocket.on("new_message", onNewMessage);
        mSocket.connect();

        dialogId = getIntent().getStringExtra("dialog id");
        messageBody = findViewById(R.id.text_body);
        sendImg = findViewById(R.id.send_msg);
        sendImg.setOnClickListener(this::onClick);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        messageRecycler = findViewById(R.id.chat_recycler);
        messageRecycler.setHasFixedSize(true);
        messageRecycler.setLayoutManager(linearLayoutManager);

        getAllMessage();
    }

    private void onClick(View v){

        sendMessage("all", messageBody.getText().toString());
        addMessage(messageBody.getText().toString(), String.valueOf(Message.USER_MSG));
    }
    private Boolean getAllMessage(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<MessageModel> call = service.getAllMessages(dialogId, token);
        call.enqueue(new Callback<MessageModel>() {

            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                MessageModel messageModel = response.body();
                MessagePayload messagePayload = messageModel.getMessagePayload();
                List<Message> messageList = messagePayload.getMessages();
                messageAdapter = new MessageAdapter(getApplicationContext(), messageList);
                messageRecycler.setAdapter(messageAdapter);

            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.printStackTrace();
            }
        });
        return  null;
    }

//    private void attemptSend() {
//        if (!mSocket.connected()) return;
//        String message = messageBody.getText().toString().trim();
//        if (TextUtils.isEmpty(message)) {
//            messageBody.requestFocus();
//            return;
//        }
//        messageBody.setText("");
//        addMessage(message);
//        mSocket.emit("new_message", message);
//    }
    private void addMessage(String message, String type) {
        Message message1= new Message();
        message1.setText(message);
        message1.setType(type);
        mMessages.add(message1);
        messageAdapter.update(message1);
        scrollToBottom();
    }

    private Emitter.Listener onNewMessage = args -> runOnUiThread(() -> {
        String message, type;
        try {
            JSONArray jsonArray = new JSONArray(Arrays.toString(args));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            message = jsonObject.getString("text");
            type = String.valueOf(Message.OTHER_MSG);
            addMessage(message, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    });

    Emitter.Listener onConnectionSuccessful = args -> {
        runOnUiThread(() -> {
            JSONArray data = (JSONArray) args[0];
            Log.d("abc", ": conn dia" + data);
        });
    };
    private void scrollToBottom() {
        messageRecycler.scrollToPosition(0);
    }

    private Boolean sendMessage(String type, String text){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<MessageModel> call = service.sendMessage(dialogId,type, text, token);
        call.enqueue(new Callback<MessageModel>() {

            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {

            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.printStackTrace();
            }
        });
        return null;
    }
}
