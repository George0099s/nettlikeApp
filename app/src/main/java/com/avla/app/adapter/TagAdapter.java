package com.avla.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.R;
import com.avla.app.model.UserSingleton;

import org.json.JSONException;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagsViewHolder> {
    private ArrayList<String> tagListName;
    private ArrayList<String> tagListId;
    private Context context;
    private Activity activity;
    UserSingleton user = UserSingleton.INSTANCE;
    public TagAdapter(ArrayList<String> tagListName, ArrayList<String> tagListId, Context context, Activity activity) {
        this.tagListName = tagListName;
        this.tagListId = tagListId;
        this.context = context;
        this.activity = activity;
    }
    
    @NonNull
    @Override
    public TagAdapter.TagsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item2, parent, false);
        return new TagAdapter.TagsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagAdapter.TagsViewHolder holder, int position) {
        holder.tag.setText(tagListName.get(position));
        holder.tag.setOnClickListener(v -> {
                    if (!holder.tag.isChecked()) {
                        if(user.getTagList().length() < 3) {
                            user.getTagList().put(tagListId.get(position));
                            user.getTagsName().add(tagListName.get(position));
                            holder.tag.setChecked(true);
                            holder.tag.setCheckMarkDrawable(R.drawable.ic_checked);
                            holder.tag.setTextColor(context.getResources().getColor(R.color.blue_1071FF));
                        }else {
                            Toast.makeText(context, "You can choose only 3 tags", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        for (int i = 0; i < user.getTagList().length(); i++) {
                            try {
                                if(user.getTagList().get(i).equals(tagListId.get(position))){
                                    holder.tag.setChecked(false);
                                    user.getTagList().remove(i);
                                    user.getTagsName().remove(i);
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

    public class TagsViewHolder extends  RecyclerView.ViewHolder{

        CheckedTextView tag;

        public TagsViewHolder(@NonNull View itemView) {
            super(itemView);
            tag = itemView.findViewById(R.id.tag_tv);
        }
    }
}
