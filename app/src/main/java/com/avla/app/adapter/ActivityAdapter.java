package com.avla.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.model.Token;
import com.avla.app.model.UserSingleton;
import com.avla.app.model.activity.Payload;
import com.avla.app.model.activity.Target;
import com.avla.app.model.activity.TargetComment;
import com.avla.app.model.activity.TargetEvent;
import com.avla.app.model.activity.TargetPost;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

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
    public ActivityAdapter(List<Payload> targets, Context context, String token) {
        this.targets = targets;
        this.context = context;
        this.token = token;
        user = UserSingleton.INSTANCE;
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
            case Target.TYPE_EVENT:
                layout = R.layout.item_activity_event;
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
                TargetPost targetPost = target.getTargetPost();
                holder.postBody.setText(targetPost.getName());
                holder.postName.setText(targetPost.getDescription());
                    if (targetPost.getPictureUrl() != null)
                    Glide.with(context).load(targetPost.getPictureUrl()).apply(RequestOptions.circleCropTransform()).into(holder.postUserImg);
                break;
            case Constants.NEW_FOLLOWER:
                String nameFollower = target.getTargetFollower().getFirstName() + " " + target.getTargetFollower().getLastName();
                holder.followerName.setText(nameFollower);
                String id = target.getTargetFollower().getId();
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
                holder.commentName.setText(nameComment);
                holder.commentBody.setText(String.format("New comment: %s", targetComment.getDescription()));
                Glide.with(context).load(targetComment.getCreatedBy().getPictureUrl()).apply(RequestOptions.circleCropTransform()).into(holder.commentUserImg);
                break;
            case Constants.NEW_EVENT:
                TargetEvent targetEvent = target.getTargetEvent();
                String nameEvent = targetEvent.getName();
                holder.eventName.setText(nameEvent);
                holder.eventBody.setText(String.format("Created new  event: %s", targetEvent.getDescription()));
                Glide.with(context).load(targetEvent.getCreatedBy().getPictureUrl()).apply(RequestOptions.circleCropTransform()).into(holder.eventUserImg);
                Glide.with(context).load(targetEvent.getPhotos().get(0)).apply(requestOptions).into(holder.eventImg);
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
        TextView eventName, eventBody, eventTime;
        ImageView postUserImg, followerUserImg, commentUserImg, eventUserImg, eventImg;
        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            postBody = itemView.findViewById(R.id.activity_post_body);
            postName = itemView.findViewById(R.id.activity_post_name);
            postTime = itemView.findViewById(R.id.activity_post_time);
            postUserImg = itemView.findViewById(R.id.activity_post_user_img);

            followerName = itemView.findViewById(R.id.activity_follower_name);
            followerBody = itemView.findViewById(R.id.activity_follower_body);
            followerTime = itemView.findViewById(R.id.activity_follower_time);
            followerUserImg = itemView.findViewById(R.id.activity_follower_user_img);
            follow = itemView.findViewById(R.id.activity_follower_follow);

            commentName = itemView.findViewById(R.id.activity_comment_name);
            commentBody = itemView.findViewById(R.id.activity_comment_body);
            commentTime = itemView.findViewById(R.id.activity_comment_time);
            commentUserImg = itemView.findViewById(R.id.activity_comment_user_img);

            eventName = itemView.findViewById(R.id.activity_event_name);
            eventBody = itemView.findViewById(R.id.activity_event_body);
            eventTime = itemView.findViewById(R.id.activity_event_time);
            eventUserImg = itemView.findViewById(R.id.activity_event_user_img);
            eventImg = itemView.findViewById(R.id.activity_event_img);
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
            case Constants.NEW_EVENT:
                type = Target.TYPE_EVENT;
                return type;
            case Constants.NEW_COMMENT:
                type = Target.TYPE_COMMENT;
                return type;
           case Constants.NEW_FOLLOWER:
               type = Target.TYPE_FOLLOWER;
               return type;
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
