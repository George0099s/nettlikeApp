package com.avla.app.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.R;
import com.avla.app.model.UserSingleton;

import org.json.JSONArray;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagsViewHolder> {
    private ArrayList<String> tagListName;
    private ArrayList<String> tagListId;
    private JSONArray tagJsonList;
    private Context context;
    private SharedPreferences sharedPreferences;
    public TagAdapter(ArrayList<String> tagListName, ArrayList<String> tagListId, JSONArray tagJsonList, Context context, SharedPreferences sharedPreferences) {
        this.tagListName = tagListName;
        this.tagListId = tagListId;
        this.tagJsonList = tagJsonList;
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }
    public TagAdapter(ArrayList<String> tagListName, ArrayList<String> tagListId){
        this.tagListName = tagListName;
        this.tagListId = tagListId;
    }
    @NonNull
    @Override
    public TagAdapter.TagsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);
        return new TagAdapter.TagsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagAdapter.TagsViewHolder holder, int position) {
        holder.tag.setText(tagListName.get(position));
        holder.tag.setOnClickListener(v -> {
            UserSingleton user = UserSingleton.INSTANCE;
            user.getTagList().put(tagListId.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return tagListName.size();
    }

    public class TagsViewHolder extends  RecyclerView.ViewHolder{

        Button tag;
        public TagsViewHolder(@NonNull View itemView) {
            super(itemView);
            tag = itemView.findViewById(R.id.tag_tv);
        }
    }
}
