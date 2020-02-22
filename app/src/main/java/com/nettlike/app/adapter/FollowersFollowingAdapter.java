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

import com.avla.app.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nettlike.app.Constants;
import com.nettlike.app.Interface.IServer;
import com.nettlike.app.model.Follower;
import com.nettlike.app.model.Token;
import com.nettlike.app.model.UserSingleton;
import com.nettlike.app.view.main.peopleInner.AnotherUserProfileActivity;

import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

public class FollowersFollowingAdapter extends RecyclerView.Adapter<FollowersFollowingAdapter.FollowViewHolder> {
    private static final String TAG = "FollowersFollowingAdapt";
    private List<Follower> followers;
    private String token;
    private Context context;
    private UserSingleton user = UserSingleton.INSTANCE;

    public FollowersFollowingAdapter(List<Follower> followers, String token, Context context) {
        this.followers = followers;
        this.token = token;
        this.context = context;
    }

    @NonNull
    @Override
    public FollowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follower, parent, false);
        return new FollowersFollowingAdapter.FollowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowViewHolder holder, int position) {
        Follower follower = followers.get(position);
        holder.followerName.setText(String.format("%s %s", follower.getFirstName(), follower.getLastName()));
        if (follower.getCity() != null || follower.getCountry() != null ){
        holder.followerLocation.setText(String.format("%s,%s", follower.getCountry(), follower.getCity()));
        } else {
            holder.followerLocation.setText(R.string.no_data);
        }
        Glide.with(context).load(follower.getPictureUrl()).apply(RequestOptions.circleCropTransform()).into(holder.followerImg);
        holder.item.setOnClickListener(v -> {
            Intent intent = new Intent(context, AnotherUserProfileActivity.class);
            intent.putExtra("token", token);
            intent.putExtra("user id", follower.getId());
            context.startActivity(intent);
        });
        if (follower.getId().contains(user.getUserId())) {
            holder.followBtn.setVisibility(View.GONE);
        } else {
            if(follower.getFollowers().contains(user.getUserId())){
                holder.followBtn.setText(R.string.unfollow_string);
            } else {
                holder.followBtn.setText(R.string.follow_string);
            }
                holder.followBtn.setOnClickListener(v -> {
                    if (follower.getFollowers().contains(user.getUserId())) {
                        follower.getFollowers().remove(user.getUserId());
                    } else {
                        follower.getFollowers().add(user.getUserId());
                    }
                    Observable.fromCallable((new CallableStartStopFollow(follower.getId())))
                            .subscribeOn(Schedulers.io())
                            .subscribe();
                });
        }
    }

    @Override
    public int getItemCount() {
        return followers.size();
    }

    public class FollowViewHolder extends RecyclerView.ViewHolder {
        private ImageView followerImg;
        private TextView followerName, followerLocation;
        private Button followBtn;
        private View item;
        public FollowViewHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.item_follower_following);
            followerImg = itemView.findViewById(R.id.follower_img);
            followerLocation = itemView.findViewById(R.id.follower_location);
            followerName = itemView.findViewById(R.id.follower_username);
            followBtn = itemView.findViewById(R.id.follow_btn);
        }
    }

    private Boolean startStopFollow(String userId) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();
        IServer service = retrofit.create(IServer.class);
        Call<Token> call = service.followUnfollow(userId, token);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });
        return true;
    }
    private class CallableStartStopFollow implements Callable<Boolean>{
        public CallableStartStopFollow(String userId) {
            this.userId = userId;
        }

        String userId;
        @Override
        public Boolean call() throws Exception {
            return startStopFollow(userId);
        }
    }
}
