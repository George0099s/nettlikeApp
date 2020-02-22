package com.nettlike.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.R;
import com.nettlike.app.model.Comment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>  {
    private static final String TAG = "CommentsAdapter";
    private List<Comment> comments;
    private Context context;
    public CommentsAdapter(List<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;

    }


    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentsAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.createdBy.setText(comment.getName());
        holder.text.setText(comment.getDescription());
        holder.commTime.setText(comment.getCreatedAt());
        Glide.with(context).load(comment.getCreatedBy().getPictureUrl()).apply(RequestOptions.circleCropTransform()).into(holder.createdByImage);


    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView createdBy, text, commTime;
        ImageView createdByImage;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            createdBy = itemView.findViewById(R.id.user_comment_name);
            text = itemView.findViewById(R.id.user_comment_text);
            createdByImage = itemView.findViewById(R.id.user_comment_img);
            commTime = itemView.findViewById(R.id.comm_time);
        }
    }
    public void addComment(Comment comment){
        comments.add(comment);
        notifyDataSetChanged();
    }
}
