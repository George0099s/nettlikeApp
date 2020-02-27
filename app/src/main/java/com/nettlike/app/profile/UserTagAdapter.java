package com.nettlike.app.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nettlike.app.R;

import org.json.JSONArray;
import org.json.JSONException;

public class UserTagAdapter  extends RecyclerView.Adapter<UserTagAdapter.TagsViewHolder> {
//    private ArrayList<String> tagListName = new ArrayList<>();
    private JSONArray tagListName;
    private Context context;

    public UserTagAdapter(JSONArray tagListName, Context context) {
        this.tagListName = tagListName;
        this.context = context;
    }

    @NonNull
    @Override
    public TagsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);
        return new UserTagAdapter.TagsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagsViewHolder holder, int position) {

        try {
            holder.tag.setText(tagListName.getString(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return tagListName.length();
    }

    public class TagsViewHolder extends  RecyclerView.ViewHolder{

        TextView tag;
        public TagsViewHolder(@NonNull View itemView) {
            super(itemView);
            tag = itemView.findViewById(R.id.tag_tv);
        }
    }
}
