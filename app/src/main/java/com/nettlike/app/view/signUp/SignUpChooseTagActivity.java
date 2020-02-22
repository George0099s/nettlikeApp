package com.nettlike.app.view.signUp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nettlike.app.R;
import com.nettlike.app.adapter.TagAdapter;
import com.nettlike.app.model.ModelTag;

import java.util.ArrayList;

public class SignUpChooseTagActivity extends AppCompatActivity {
    private static final String TAG = "SignUpChooseTagActivity";
    private String parentId;
    private String token;
    private ArrayList<String> tagListName = new ArrayList<>();
    private ArrayList<String> tagListId = new ArrayList<>();
    private RecyclerView tagRecyclerView;
    private TagAdapter tagAdapter;
    private Activity activity;
    private Button addTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_choose_tag);
        tagRecyclerView = findViewById(R.id.choose_tag_recycler);
        activity = this;
        addTags = findViewById(R.id.add_tag_btn);
        addTags.setOnClickListener(this::onClick);
        parentId = getIntent().getStringExtra("parent id");
        token = getIntent().getStringExtra("token");
        getTagsById(parentId);

    }

    private void onClick(View view) {
        finish();
    }


    private Boolean getTagsById(String parentId){

        for (int i = 0; i < ModelTag.tagsPayload.size(); i++) {
                        ArrayList<String> parentList= ModelTag.tagsPayload.get(i).getParentIds();
                        ModelTag.ParenIds = parentList;
                        if (parentList.size() != 0 && parentList.contains(parentId)) {
                            tagListName.add(ModelTag.tagsPayload.get(i).getName());
                            tagListId.add(ModelTag.tagsPayload.get(i).getId());
                        }

                    }
            tagAdapter = new TagAdapter(tagListName, tagListId, getApplicationContext(), activity);
            tagRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            tagRecyclerView.setAdapter(tagAdapter);

        return true;
    }
}
