package com.nettlike.app.posts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.nettlike.app.Constants;
import com.nettlike.app.Interface.IServer;
import com.nettlike.app.R;
import com.nettlike.app.data.AppDatabase;
import com.nettlike.app.data.TokenDao;
import com.nettlike.app.model.ModelPostComms;
import com.nettlike.app.model.Post;
import com.nettlike.app.model.PostComsPayload;
import com.nettlike.app.model.Token;
import com.nettlike.app.model.UserSingleton;
import com.nettlike.app.people.ui.AnotherUserProfileActivity;
import com.nettlike.app.profile.UserTagAdapter;

import org.json.JSONArray;

import java.util.ArrayList;
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
    private ImageView postImage, sendComment, postUserImage, like, comm;
    private String token, postId, postPitureUrl, userId;
    private TextView postName, postDescription, postUserName, likeCount, commCount;
    private CommentsAdapter commentsAdapter;
    private RecyclerView commentsRecycler;
    private LinearLayoutManager linearLayoutManager;
    private UserSingleton user;
    private RequestOptions requestOptions;
    private AppDatabase db;
    private TokenDao tokenDao;
    private RecyclerView tagRecycler;
    private UserTagAdapter userTagAdapter;
    private ArrayList<String> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_info);
        initView();
    }

    private void initView() {

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "avla_DB")
                .allowMainThreadQueries()
                .build();
        tokenDao = db.tokenDao();
        token = String.valueOf(tokenDao.getToken().get(0));
        like = findViewById(R.id.like_img_post_info);
        like.setOnClickListener(this::onClick);
        likeCount = findViewById(R.id.like_count_post_info);
        commCount = findViewById(R.id.comm_count_post_info);
        tagRecycler = findViewById(R.id.post_info_recycler);
        tagRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        tagRecycler.setHasFixedSize(true);
        requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(Constants.IMG_ROUND_CORNER));
        postUserImage = findViewById(R.id.post_info_user_img);
        postUserName = findViewById(R.id.post_info_user_name);
        postUserName.setOnClickListener(this::onClick);
        postUserImage.setOnClickListener(this::onClick);
        commentBody = findViewById(R.id.comm_body);
        sendComment = findViewById(R.id.send_comm);
        sendComment.setOnClickListener(this::onClick);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        user = UserSingleton.INSTANCE;
        postPitureUrl = getIntent().getStringExtra("post_picture_url");
        post = getIntent().getParcelableExtra("post");
        tags = getIntent().getStringArrayListExtra("tags_array");
        postId = getIntent().getStringExtra("post_id");
        if (postPitureUrl != null)
        post.setPictureUrl(postPitureUrl);
        postImage = findViewById(R.id.info_post_img);
        postName = findViewById(R.id.info_post_name);
        postDescription = findViewById(R.id.info_post_description);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        commentsRecycler = findViewById(R.id.post_comments_recycler);
        commentsRecycler.setHasFixedSize(true);
        commentsRecycler.setLayoutManager(linearLayoutManager);
        userName = "123";
        getPostInfo();
        Observable.fromCallable(new CallableGetComms(postId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_comm:
                if (commentBody.getText() == null || commentBody.getText().toString().equals("")) {
                    commentBody.requestFocus();
                    commentBody.setError("You can't add empty comment");
                } else {
                    sendComment(postId, userName, commentBody.getText().toString());
                    commentBody.setText("");
                    if (commentsAdapter != null)
                        commentsAdapter.notifyDataSetChanged();
                    else
                        Toast.makeText(this, "Wait please", Toast.LENGTH_SHORT).show();
                    Observable.fromCallable(new CallableGetComms(postId))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe();

                }
                break;
            case R.id.post_info_user_name:
            case R.id.post_info_user_img:
                Intent intent = new Intent(PostInfoActivity.this, AnotherUserProfileActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("user id", userId);
                startActivity(intent);
                break;
            case R.id.like_img_post_info:
                markAsSave(token, postId);
                if (user.getSavedPosts().contains(postId)) {
                    like.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                    user.getSavedPosts().remove(postId);
                    likeCount.setText(String.valueOf(post.getSavedCount() - 1));
                } else {
                    like.setImageDrawable(getResources().getDrawable(R.drawable.ic_liked));
                    user.getSavedPosts().add(postId);
                    likeCount.setText(String.valueOf(post.getSavedCount() + 1));
                }
                break;
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
                Post post = payload.getPost();
                commentsAdapter = new CommentsAdapter(payload.getComments(), getApplicationContext());
                commentsRecycler.setAdapter(commentsAdapter);
                postUserName.setText(String.format("%s %s", post.getCreatedBy().getFirstName(), post.getCreatedBy().getLastName()));
                userId = post.getCreatedBy().getId();
                Glide.with(PostInfoActivity.this).load(post.getCreatedBy().getPictureUrl()).apply(RequestOptions.circleCropTransform()).into(postUserImage);
                if (post.getPictureUrl() != null){
                    Glide.with(PostInfoActivity.this).load(post.getPictureUrl()).apply(requestOptions).into(postImage);
                    postImage.setVisibility(View.VISIBLE);
                }
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
                commCount.setText(postComsPayload.getPost().getCommentsCount());
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
        postUserName.setText(userName);
        commCount.setText(post.getCommentsCount());
        likeCount.setText(String.valueOf(post.getSavedCount()));
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < tags.size(); i++) {
            jsonArray.put(tags.get(i));
        }
        userTagAdapter = new UserTagAdapter(jsonArray, getApplicationContext());
        tagRecycler.setAdapter(userTagAdapter);
        if (user.getSavedPosts().contains(postId)) {
            like.setImageDrawable(getResources().getDrawable(R.drawable.ic_liked));
        } else {
            like.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
        }
        if (post.getPictureUrl() == null || postPitureUrl == null) {
//            postImage.setVisibility(View.GONE);
        } else {
            Glide.with(this).load(post.getPictureUrl()).apply(requestOptions).into(postImage);
            postImage.setVisibility(View.VISIBLE);
        }
    }
    class CallableGetComms implements Callable<Boolean> {

        private final String postId;

        public CallableGetComms(String postId) {
            this.postId = postId;
        }

        @Override
        public Boolean call() throws Exception {
            return getComms(postId);
        }
    }

    private Boolean markAsSave(String token, String postId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<Token> call = service.saveDeletePost(postId, token);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {

            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });
        return true;
    }
}
