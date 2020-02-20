package com.avla.app.view.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.adapter.CommentsAdapter;
import com.avla.app.model.ModelPostComms;
import com.avla.app.model.Post;
import com.avla.app.model.PostComsPayload;
import com.avla.app.model.UserSingleton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PostInfoActivity extends AppCompatActivity {
    private static final String TAG = "PostInfoActivity";
    private EditText commentBody;
    private Post post;
    private String userName;
    private ImageView postImage, sendComment;
    private String token, postId;
    private TextView postName, postDescription;
    private CommentsAdapter commentsAdapter;
    private RecyclerView commentsRecycler;
    private LinearLayoutManager linearLayoutManager;
    private UserSingleton user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_info);
        initView();
    }

    private void initView() {
        commentBody = findViewById(R.id.comm_body);
        sendComment = findViewById(R.id.send_comm);
        sendComment.setOnClickListener(this::onClick);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        user = UserSingleton.INSTANCE;
        token = getIntent().getStringExtra("token");
        post = getIntent().getParcelableExtra("post");
        postImage = findViewById(R.id.info_post_img);
        postName = findViewById(R.id.info_post_name);
        postDescription = findViewById(R.id.info_post_description);
        postId = getIntent().getStringExtra("post_id");
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        commentsRecycler = findViewById(R.id.post_comments_recycler);
        commentsRecycler.setHasFixedSize(true);
        commentsRecycler.setLayoutManager(linearLayoutManager);
        userName = user.getFirstName() + " " + user.getLastName();
        getPostInfo();
        Observable.fromCallable(new CallableLongAction(postId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void onClick(View view) {
        if(commentBody.getText() == null || commentBody.getText().toString().equals("")) {
            commentBody.requestFocus();
            commentBody.setError("You can't add empty comment");
        } else {
            sendComment(postId, userName, commentBody.getText().toString());
            commentBody.setText("");
            commentsAdapter.notifyDataSetChanged();
            Observable.fromCallable(new CallableLongAction(postId))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();

        }
    }

    private boolean getComms(String postId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<ModelPostComms> call = service.getPostInfo(postId, token);

        call.enqueue(new Callback<ModelPostComms>() {

            @Override
            public void onResponse(Call<ModelPostComms> call, Response<ModelPostComms> response) {
                ModelPostComms modelPost = response.body();
                PostComsPayload payload = modelPost.getPayload();
                commentsAdapter = new CommentsAdapter(payload.getComments(), getApplicationContext());
                commentsRecycler.setAdapter(commentsAdapter);
                Log.d(TAG, "initView: " + Thread.currentThread().getName());
            }

            @Override
            public void onFailure(Call<ModelPostComms> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });

        return true;
    }

    private boolean sendComment(String postId, String name, String text){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<ModelPostComms> call = service.sendComment(postId, token, name, text);

        call.enqueue(new Callback<ModelPostComms>() {
            @Override
            public void onResponse(Call<ModelPostComms> call, Response<ModelPostComms> response) {

                ModelPostComms messageModel = response.body();
                PostComsPayload postComsPayload = messageModel.getPayload();
            }

            @Override
            public void onFailure(Call<ModelPostComms> call, Throwable t) {
                Log.d(TAG, "onFailure:3 " + t.getMessage());
                t.printStackTrace();
            }
        });
        return true;

    }

    private void getPostInfo() {
        postName.setText(post.getName());
        postDescription.setText(post.getDescription());
            RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
        if (post.getPictureUrl() == null) {
            postImage.setVisibility(View.GONE);
        } else
            Glide.with(this).load(post.getPictureUrl()).apply(requestOptions).into(postImage);
    }
    class CallableLongAction implements Callable<Boolean> {

        private final String postId;

        public CallableLongAction(String postId) {
            this.postId = postId;
        }

        @Override
        public Boolean call() throws Exception {
            return getComms(postId);
        }
    }

}
