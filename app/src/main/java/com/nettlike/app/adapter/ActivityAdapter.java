package com.nettlike.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.nettlike.app.CalendarHelper;
import com.nettlike.app.Constants;
import com.nettlike.app.Interface.IServer;
import com.nettlike.app.R;
import com.nettlike.app.model.Post;
import com.nettlike.app.model.Token;
import com.nettlike.app.model.UserSingleton;
import com.nettlike.app.model.activity.Payload;
import com.nettlike.app.model.activity.Target;
import com.nettlike.app.model.activity.TargetComment;
import com.nettlike.app.model.activity.TargetFollower;
import com.nettlike.app.view.main.PostInfoActivity;
import com.nettlike.app.view.main.peopleInner.AnotherUserProfileActivity;

import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {
    private static final String TAG = "ActivityAdapter";
    private List<Payload> targets;
    private Context context;
    private UserSingleton user;
    private String token;
    private RequestOptions requestOptions;
    private CalendarHelper calendarHelper;
    public ActivityAdapter(List<Payload> targets, Context context, String token) {
        this.targets = targets;
        this.context = context;
        this.token = token;
        user = UserSingleton.INSTANCE;
        calendarHelper = new CalendarHelper();
        requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
    }
//    private List<>

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = -1;
        switch (viewType) {
            case Target.TYPE_COMMENT:
                layout = R.layout.item_activity_comment;
                break;
            case Target.TYPE_POST:
                layout = R.layout.item_activity_post;
                break;
            case Target.TYPE_FOLLOWER:
                layout = R.layout.item_activity_follower;
                break;
        }
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);
        return new ActivityAdapter.ActivityViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        Payload target = targets.get(position);
        if (target != null)
        switch (target.getType()){
            case Constants.NEW_POST:
                Post targetPost = target.getTargetPost();
                holder.postBody.setText(targetPost.getName());
                holder.postName.setText(targetPost.getDescription());
                holder.postTime.setText(calendarHelper.getIntervaledMessageTime(targetPost.getCreatedAt()));
                    if (targetPost.getPictureUrl() != null)
                    Glide.with(context).load(targetPost.getPictureUrl()).apply(RequestOptions.circleCropTransform()).into(holder.postUserImg);
                    holder.postName.setOnClickListener(v -> {
                        Intent intent = new Intent(context, AnotherUserProfileActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("user id", target.getTargetPost().getCreatedBy().getId());
                        context.startActivity(intent);
                    });
                    holder.postItem.setOnClickListener(v -> {
                    Intent intent = new Intent(context, PostInfoActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("post id", targetPost.getId());
                    intent.putExtra("post", targetPost);
                    context.startActivity(intent);
                });
                    break;
            case Constants.NEW_FOLLOWER:
                TargetFollower follower = target.getTargetFollower();
                holder.followerItem.setOnClickListener(v -> {
                    Intent intent = new Intent(context, AnotherUserProfileActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("user id", follower.getId());
                    context.startActivity(intent);
                });
                String nameFollower = follower.getFirstName() + " " + follower.getLastName();
                holder.followerName.setText(nameFollower);
                String id = target.getTargetFollower().getId();
                holder.followerTime.setText(calendarHelper.getIntervaledMessageTime(follower.getCreatedAt()));
                holder.follow.setOnClickListener(v -> {
                    Observable.fromCallable(new CallableStartFollow(id))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe();
                    if (user.getFollowing().contains(id)) {
                        user.getFollowing().remove(id);
                        } else {
                        user.getFollowing().add(id);
                    }
                    notifyDataSetChanged();
                });
                if (user.getFollowing().contains(id)){
                    holder.follow.setText(context.getString(R.string.unfollow_string));
                } else {
                    holder.follow.setText(context.getString(R.string.follow_string));
                }
                    if (target.getTargetFollower().getPictureUrl() != null)
                    Glide.with(context).load(target.getTargetFollower().getPictureUrl()).apply(RequestOptions.circleCropTransform()).into(holder.followerUserImg);
                break;
            case Constants.NEW_COMMENT:
                TargetComment targetComment = target.getTargetComment();
                String nameComment = targetComment.getName();
                holder.commentItem.setOnClickListener(v -> {
//                        Intent intent = new Intent(context, PostInfoActivity.class);
//                        intent.putExtra("token", token);
//                        intent.putExtra("post id", targetComment.get.getId());
//                        intent.putExtra("post", targetPost);
//                        context.startActivity(intent);
                });
                holder.commentName.setText(nameComment);
                holder.commentBody.setText(String.format("New comment: %s", targetComment.getDescription()));
                holder.commentTime.setText(calendarHelper.getIntervaledMessageTime(targetComment.getCreatedAt()));
                Glide.with(context).load(targetComment.getCreatedBy().getPictureUrl()).apply(RequestOptions.circleCropTransform()).into(holder.commentUserImg);
                break;

        }

    }

    @Override
    public int getItemCount() {
        return targets.size();
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView postName, postBody, postTime;
        TextView followerName, followerBody, followerTime;
        Button follow;
        TextView commentName, commentBody, commentTime;
        ImageView postUserImg, postImg, followerUserImg, commentUserImg;
        View postItem, followerItem, commentItem;
        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            postBody = itemView.findViewById(R.id.activity_post_body);
            postName = itemView.findViewById(R.id.activity_post_name);
            postTime = itemView.findViewById(R.id.activity_post_time);
            postUserImg = itemView.findViewById(R.id.activity_post_user_img);
            postItem = itemView.findViewById(R.id.item_post);

            followerName = itemView.findViewById(R.id.activity_follower_name);
            followerBody = itemView.findViewById(R.id.activity_follower_body);
            followerTime = itemView.findViewById(R.id.activity_follower_time);
            followerUserImg = itemView.findViewById(R.id.activity_follower_user_img);
            follow = itemView.findViewById(R.id.activity_follower_follow);
            followerItem = itemView.findViewById(R.id.item_follower);

            commentName = itemView.findViewById(R.id.activity_comment_name);
            commentBody = itemView.findViewById(R.id.activity_comment_body);
            commentTime = itemView.findViewById(R.id.activity_comment_time);
            commentUserImg = itemView.findViewById(R.id.activity_comment_user_img);
            commentItem = itemView.findViewById(R.id.item_comment);

        }

    }

    @Override
    public int getItemViewType(int position) {
        int type;
       switch (targets.get(position).getType()){
            case Constants
                    .NEW_POST:
                type = Target.TYPE_POST;
                return  type;
           case Constants.NEW_COMMENT:
                type = Target.TYPE_COMMENT;
                return type;
           case Constants.NEW_FOLLOWER:
               type = Target.TYPE_FOLLOWER;
               return type;
           case Constants.NEW_EVENT:
               return 4;
           default: return -1;
       }

    }
    private Integer startStopFollow(String id) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();
        IServer service = retrofit.create(IServer.class);
        Call<Token> call = service.followUnfollow(id, token);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Log.d(TAG, "onResponse: start following" );

            }
            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });
        return null;
    }
    private class CallableStartFollow implements Callable<Integer>{
        public CallableStartFollow(String id) {
            this.id = id;
        }

        String id;
        @Override
        public Integer call() throws Exception {
            return startStopFollow(id);
        }

    }

}
