package com.nettlike.app.view.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nettlike.app.Constants;
import com.nettlike.app.Interface.IServer;
import com.nettlike.app.R;
import com.nettlike.app.adapter.PostTagsAdapter;
import com.nettlike.app.model.ModelTag;

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

public class GetTagsActivity extends AppCompatActivity {
    private static final String TAG = "GetTagsActivity";
    private ArrayList<String> tagListName;
    private ArrayList<String> tagListId;
    private JSONArray postTagId = new JSONArray();
    private ArrayList<String> postTagName = new ArrayList<>();
    private RecyclerView tagRecyclerView;
    private PostTagsAdapter tagAdapter;
    private String token;
    private Activity activity;
    private Button addTags;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_tags);

        initView();
    }

    private void initView() {
        token = getIntent().getStringExtra("token");
        tagListName = getIntent().getStringArrayListExtra("tags_name");
        tagListId = getIntent().getStringArrayListExtra("tags_id");
        activity = this;
        Observable.fromCallable(new CallableGetTags(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        addTags = findViewById(R.id.add_post_tag);
        addTags.setOnClickListener(this::onClick);
        tagRecyclerView = findViewById(R.id.tag_another_reycler);
    }


    private void onClick(View view) {
            Intent intent = new Intent();
            intent.putExtra("tags_id", tagAdapter.getTagsId());
            intent.putExtra("tags_name",tagAdapter.getPostTagsNam());
            setResult(42, intent);
            finish();


    }

    private boolean getTags(String token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<ModelTag> call = service.getAllTags(token);
        call.enqueue(new Callback<ModelTag>() {
            @Override
            public void onResponse(Call<ModelTag> call, Response<ModelTag> response) {
                ModelTag modelTag = response.body();
                ModelTag.tagsPayload = modelTag.getPayload();
                for (int i = 0; i <ModelTag.tagsPayload.size(); i++) {
                    ArrayList<String> parentList= ModelTag.tagsPayload.get(i).getParentIds();
                    ModelTag.ParenIds = parentList;
                    if (ModelTag.tagsPayload.get(i).getParentIds().size() == 0 ) {
                        tagListName.add(ModelTag.tagsPayload.get(i).getName());
                        tagListId.add(ModelTag.tagsPayload.get(i).getId());
                    }
                }
                tagAdapter = new PostTagsAdapter(tagListName, tagListId,postTagName, postTagId, getApplicationContext(), activity);
                tagRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                tagRecyclerView.setAdapter(tagAdapter);
            }

            @Override
            public void onFailure(retrofit2.Call<ModelTag> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });
        return true;
    }


    private class CallableGetTags implements Callable {
        private String token;

        private CallableGetTags(String token) {
            this.token = token;
        }

        @Override
        public Object call() throws Exception {
            return getTags(token);
        }
    }

}
