package com.nettlike.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nettlike.app.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class PostTagsAdapter extends RecyclerView.Adapter<PostTagsAdapter.TagsViewHolder> {
    private ArrayList<String> tagListName;
    private ArrayList<String> tagListId;
    private ArrayList<String> postTagsName;
    private JSONArray postTagsId;
    private Context context;
    private Activity activity;

    public PostTagsAdapter(ArrayList<String> tagListName, ArrayList<String> tagListId, ArrayList<String> postTags, JSONArray postTagsId, Context context, Activity activity) {
        this.tagListName = tagListName;
        this.tagListId = tagListId;
        this.postTagsName = postTags;
        this.postTagsId = postTagsId;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TagsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item2, parent, false);
        return new PostTagsAdapter.TagsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagsViewHolder holder, int position) {
        holder.tag.setText(tagListName.get(position));
        holder.tag.setOnClickListener(v -> {
            if (!holder.tag.isChecked()) {
                if(postTagsId.length() < 3) {
                    postTagsId.put(tagListId.get(position));
                    postTagsName.add(tagListName.get(position));
                    holder.tag.setChecked(true);
                    holder.tag.setCheckMarkDrawable(R.drawable.ic_checked);
                    holder.tag.setTextColor(context.getResources().getColor(R.color.blue_1071FF));
                }else {
                    Toast.makeText(context, "You can choose only 3 tags", Toast.LENGTH_SHORT).show();
                }
            } else {
                for (int i = 0; i < postTagsName.size(); i++) {
                    try {
                        if(postTagsId.get(i).equals(tagListId.get(position))){
                            holder.tag.setChecked(false);
                            postTagsId.remove(i);
                            postTagsName.remove(i);
                            holder.tag.setCheckMarkDrawable(null);
                            holder.tag.setTextColor(context.getResources().getColor(R.color.black000));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tagListName.size();
    }

    public class TagsViewHolder extends RecyclerView.ViewHolder {
        CheckedTextView tag;
        public TagsViewHolder(@NonNull View itemView) {
            super(itemView);
            tag = itemView.findViewById(R.id.tag_tv);
        }
    }
    public ArrayList<String> getTagsId(){
        ArrayList<String> tagsId = new ArrayList<>();
        for (int i = 0; i < postTagsId.length(); i++) {
            try {
                tagsId.add(String.valueOf(postTagsId.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tagsId;
    }

    public ArrayList<String> getPostTagsNam(){
        return postTagsName;
    }
}
