package com.nettlike.app.posts;

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
import com.nettlike.app.model.PayloadTag;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class PostTagsAdapter extends RecyclerView.Adapter<PostTagsAdapter.TagsViewHolder> {
    private static final String TAG = "PostTagsAdapter";
    private ArrayList<String> tagListName;
    private ArrayList<String> tagListId;
    private ArrayList<String> postTagsName;
    private JSONArray postTagsId;
    private Context context;
    private Activity activity;
    private List<PayloadTag> payloadTagList;
    private List<PayloadTag> selectedTags;
//
//    public PostTagsAdapter(ArrayList<String> tagListName, ArrayList<String> tagListId, ArrayList<String> postTags, JSONArray postTagsId, Context context, Activity activity, List<PayloadTag> payloadTagList) {
//        this.tagListName = tagListName;
//        this.tagListId = tagListId;
//        this.postTagsName = postTags;
//        this.postTagsId = postTagsId;
//        this.context = context;
//        this.activity = activity;
//        this.payloadTagList = payloadTagList;
//    }

    public PostTagsAdapter(Context context, List<PayloadTag> payloadTagList, List<PayloadTag> selectedTags) {
        this.context = context;
        this.payloadTagList = payloadTagList;
        this.selectedTags = selectedTags;
    }
    @NonNull
    @Override
    public TagsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item2, parent, false);
        return new PostTagsAdapter.TagsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagsViewHolder holder, int position) {
        PayloadTag tag = payloadTagList.get(position);
        for (PayloadTag selected : selectedTags) {
            if (selected.getId().contains(tag.getId())) {
                holder.tag.setText(tag.getName());
                holder.tag.setChecked(true);
                holder.tag.setCheckMarkDrawable(R.drawable.ic_checked);
                holder.tag.setTextColor(context.getResources().getColor(R.color.blue_1071FF));
            }
        }
        holder.tag.setText(tag.getName());
        holder.tag.setOnClickListener(v -> {
            if (!holder.tag.isChecked()) {
                if (selectedTags.size() < 3) {
                    selectedTags.add(tag);
                    holder.tag.setChecked(true);
                    holder.tag.setCheckMarkDrawable(R.drawable.ic_checked);
                    holder.tag.setTextColor(context.getResources().getColor(R.color.blue_1071FF));
                }else {
                    Toast.makeText(context, "You can choose only 3 tags", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (selectedTags != null && selectedTags.size() != 0)
//                for (PayloadTag selected : selectedTags)
//                    if (payloadTagList.contains(selected.getId())) {
                    selectedTags.remove(tag);
                            holder.tag.setChecked(false);
                            holder.tag.setCheckMarkDrawable(null);
                            holder.tag.setTextColor(context.getResources().getColor(R.color.black000));
//                        }

//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return payloadTagList.size();
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

    public void setTagsId() {
        postTagsId = new JSONArray();
    }

    public ArrayList<String> getPostTagsNam(){
        return postTagsName;
    }

    public void setPostTagsNam() {
        postTagsName = new ArrayList<>();
    }
}
