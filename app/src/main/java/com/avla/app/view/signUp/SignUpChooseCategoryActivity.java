package com.avla.app.view.signUp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.adapter.TagAdapter;
import com.avla.app.model.ModelTag;
import com.avla.app.model.UserSingleton;

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

public class SignUpChooseCategoryActivity extends AppCompatActivity {
    private static final String TAG = "SignUpChooseCategoryAct";
    private ArrayList<String> tagListName = new ArrayList<>();
    private ArrayList<String> tagListId = new ArrayList<>();
    private RecyclerView tagRecyclerView;
    private TagAdapter tagAdapter;
    private String token;
    private UserSingleton userSingleton = UserSingleton.INSTANCE;
    private Activity activity;
    private Button addTags;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_choose_category);

        userSingleton.setTagsName(new ArrayList<>());
        tagListName = new ArrayList<>();
        tagListId = new ArrayList<>();
        activity = this;
        token = getIntent().getStringExtra("token");
        Observable.fromCallable(new CallableGetTags(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        addTags = findViewById(R.id.add_tag_btn2);
        addTags.setOnClickListener(this::onClick);
        userSingleton.setTagList(new JSONArray());
        userSingleton.setTagsName(new ArrayList<>());
        tagRecyclerView = findViewById(R.id.tag_reycler);
    }

    private void onClick(View view) {
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

                tagAdapter = new TagAdapter(tagListName, tagListId, getApplicationContext(), activity);
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
    public class CallableGetTags implements Callable {
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
