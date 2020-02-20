package com.avla.app.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.AvlaApplication;
import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.adapter.MessageAdapter;
import com.avla.app.model.Message;
import com.avla.app.model.MessageDialog;
import com.avla.app.model.MessageMember;
import com.avla.app.model.MessageModel;
import com.avla.app.model.MessagePayload;
import com.avla.app.model.UserSingleton;
import com.avla.app.view.main.peopleInner.AnotherUserProfileActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import rx.schedulers.Schedulers;

public class DialogActivity extends AppCompatActivity {
    private static final String TAG = "DialogActivity";
    private ImageView userImg;
    private JSONObject tokenJson = new JSONObject();
    private JSONObject dialogIdJson = new JSONObject();
    private String token, dialogId, userId, pictureId, anotherUserId;
    private EditText messageBody;
    private TextView isTyping, userName;
    private ImageView sendImg;
    private RecyclerView messageRecycler;
    private MessageAdapter messageAdapter;
    private Socket socket;
    private LinearLayoutManager linearLayoutManager;
    private Manager manager;
    private int offset = 0;
    private List<Message> messageList;
    private UserSingleton userSingleton;
    private int limit = 50;
    private List<Message> mMessages = new ArrayList<Message>();
    private Boolean isLoading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        initView();

    }

    private void initView() {
        token = getIntent().getStringExtra("token");
        dialogId = getIntent().getStringExtra("dialog id");
        pictureId = getIntent().getStringExtra("picture id");
        anotherUserId = getIntent().getStringExtra("user id");
        Log.d(TAG, "initView: 123 " + anotherUserId  );
        Log.d("BBB", "initView() called" +dialogId + " " + pictureId + " " +anotherUserId + ' ' + token);
        try {
            tokenJson.put("token", token);
            dialogIdJson.put("token", token);
            dialogIdJson.put("dialog_id", dialogId);
            dialogIdJson.put("account_id", UserSingleton.INSTANCE.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        userName = findViewById(R.id.user_name);
        userSingleton = UserSingleton.INSTANCE;
        userId = userSingleton.getUserId();

        AvlaApplication app = (AvlaApplication) getApplication();
        manager = app.getManager();
        socket = manager.socket("/all");

        socket.emit("create_connection", tokenJson);
        socket.on("create_connection_successful", onConnectionSuccessful);
        socket.on("new_message", onNewMessage);
        socket.on("typing_start", onTypingStart);
        socket.on("typing_stop", onTypingStop);
        socket.connect();

        messageBody = findViewById(R.id.text_body);
        messageBody.addTextChangedListener(textWatcher);
        isTyping = findViewById(R.id.is_typing);
        userImg = findViewById(R.id.user_img);
        userImg.setOnClickListener(this::onClick);
        userName.setOnClickListener(this::onClick);
        sendImg = findViewById(R.id.send_msg);
        sendImg.setOnClickListener(this::onClick);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        messageRecycler = findViewById(R.id.chat_recycler);
        messageRecycler.setHasFixedSize(true);
        messageRecycler.setLayoutManager(linearLayoutManager);
        messageRecycler.addOnScrollListener(scrollListener);
        messageRecycler.scrollToPosition(0);
        GetAllMessage getAllMessage = new GetAllMessage();
        getAllMessage.doInBackground(limit, offset);
//        seeDialog(dialogId, token);
//        rxQuery();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        GetAllMessage getAllMessage = new GetAllMessage();
        getAllMessage.doInBackground(limit, offset);
    }

    private void rxQuery(){
        Observable.just((seeDialog(dialogId, token)))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
    private Boolean seeDialog(String dialogId, String token) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<MessageModel> call = service.markDialogAsSeen(dialogId, token);

        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                Log.d(TAG, "onResponse: " +response.body());

            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Log.d(TAG, "onFailure:1 " + t.getMessage());
                t.printStackTrace();
            }
        });
        return true;
    }

    private void onClick(View v){
        Intent intent = new Intent(DialogActivity.this, AnotherUserProfileActivity.class);
        intent.putExtra("user id", anotherUserId);
        intent.putExtra("picture id",pictureId);
        intent.putExtra("token", token);
        switch (v.getId()){

            case R.id.send_msg:
                if (messageBody.getText() == null || messageBody.getText().toString().equals("")){
                    messageBody.requestFocus();
                    messageBody.setError("You can't send empty message");
                } else {
                    sendMessage("all", messageBody.getText().toString());
                    messageBody.setText("");
                    scrollToBottom();
                }
                break;
            case R.id.user_img:

            case R.id.user_name:
                startActivity(intent);
                break;

        }

    }



    private Boolean getAllMessage(int limit, int offset){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<MessageModel> call = service.getAllMessages(dialogId, limit,offset, token);

         call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {

                MessageModel messageModel = response.body();
                MessagePayload messagePayload = messageModel.getMessagePayload();
                MessageDialog dialog = messagePayload.getMessageDialog();
                List<MessageMember> members = dialog.getMembers();

                for (MessageMember member: members) {
                    if(!member.getId().equals(userId)){
                        userName.setText(String.format("%s %s", member.getFirstName(), member.getLastName()));
                    }
                }

                messageList = messagePayload.getMessages();
                if(messageList.size() != 0) {
                    if (messageAdapter == null) {
                        messageAdapter = new MessageAdapter(getApplicationContext(), messageList);
                        messageRecycler.setAdapter(messageAdapter);

                        Glide.with(getApplicationContext()).load(pictureId).apply(RequestOptions.circleCropTransform()).into(userImg);
                        isLoading = true;
                    } else {
                        messageAdapter.addAll((ArrayList<Message>) messageList);
                        isLoading = true;
                    }
                } else {
                    isLoading = false;
                }

            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Log.d(TAG, "onFailure:2 " + t.getMessage());
                t.printStackTrace();
            }
        });
        return  null;

    }



    private class GetAllMessage extends AsyncTask<Integer, Void, Void>{

        final ProgressDialog pd = new ProgressDialog(DialogActivity.this);
        @Override
        protected Void doInBackground(Integer... ints) {
            pd.setMessage("loading");
            pd.show();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASIC_URL) // Адрес сервера
                    .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                    .build();

            IServer service = retrofit.create(IServer.class);
            Call<MessageModel> call = service.getAllMessages(dialogId, ints[0],ints[1], token);

            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {

                    MessageModel messageModel = response.body();
                    MessagePayload messagePayload = messageModel.getMessagePayload();
                    messageList = messagePayload.getMessages();

//                    if(messageList.size() != 0) {
                        if (messageAdapter == null) {
                            messageAdapter = new MessageAdapter(getApplicationContext(), messageList);
                            messageRecycler.setAdapter(messageAdapter);
                            Glide.with(getApplicationContext()).load(pictureId).apply(RequestOptions.circleCropTransform()).into(userImg);
                            isLoading = true;
                        } else if(messageList.size() != 0){
                            messageAdapter.addAll((ArrayList<Message>) messageList);
                            isLoading = true;
                        }
//                    } else {
//                        isLoading = false;
//                    }
                    pd.cancel();

                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {
                    Log.d(TAG, "onFailure:3 " + t.getMessage());
                    t.printStackTrace();
                }
            });

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private Emitter.Listener onNewMessage = args -> runOnUiThread(() -> {
        String text,  createdBy, createdAt;
        int type;
        Message message;
        try {
            JSONArray jsonArray = new JSONArray(Arrays.toString(args));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            createdBy = jsonObject.getString("created_by");
            createdAt = jsonObject.getString("created_at");
            text = jsonObject.getString("text");
            type = createdBy.equals(userId) ? Message.USER_MSG : Message.OTHER_MSG;

            message = new Message();
            message.setCreatedBy(createdBy);
            message.setType(String.valueOf(type));
            message.setText(text);
            message.setCreatedAt(createdAt);
            if(message!= null) {
                messageAdapter.update(message);
            }else {
                Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_SHORT).show();
            }
            scrollToBottom();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    });

    private Emitter.Listener onConnectionSuccessful = args -> {
        runOnUiThread(() -> {
            JSONArray data = (JSONArray) args[0];
        });
    };

    private Emitter.Listener onTypingStart = args -> runOnUiThread(() -> {
        String accountId;
        try {
            JSONArray jsonArray = new JSONArray(Arrays.toString(args));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            accountId = jsonObject.getString("account_id");

            if (!accountId.equals(UserSingleton.INSTANCE.getUserId())) {
                isTyping.setVisibility(View.VISIBLE);
            }


        } catch (JSONException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }

    });

    private Emitter.Listener onTypingStop = args -> runOnUiThread(() -> {
        String accountId;
        try {
            JSONArray jsonArray = new JSONArray(Arrays.toString(args));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            accountId = jsonObject.getString("account_id");
            isTyping.setVisibility(View.GONE);
        } catch (JSONException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }

    });

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
                Log.d(TAG, "onFailure:4 " + t.getMessage());
                t.printStackTrace();
            }
        });
        return null;
    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = linearLayoutManager.getChildCount();
            int totalItemCount = linearLayoutManager.getItemCount();
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            if (isLoading) {

                if (dy % 5 == 0) {
                    if (messageList.size() != 0) {
                       final ProgressDialog pd = new ProgressDialog(DialogActivity.this);
                        pd.setMessage("loading");
                        pd.show();
                        getAllMessage(limit, offset += 50);
                        isLoading = false;
                        pd.cancel();
                    }
                }
            }
        }
    };
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            socket.emit("notify_typing_start", dialogIdJson);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            Handler handler = new Handler();
//            handler.postDelayed(() -> socket.emit("notify_typing_start", dialogIdJson), 2000);
        }

        @Override
        public void afterTextChanged(Editable s) {
            Handler handler = new Handler();
            handler.postDelayed(() -> socket.emit("notify_typing_stop", dialogIdJson), 5000);
        }
    };
}
