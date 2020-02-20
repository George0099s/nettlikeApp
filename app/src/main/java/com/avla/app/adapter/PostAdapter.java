package com.avla.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.model.ModelPost;
import com.avla.app.model.Post;
import com.avla.app.model.Token;
import com.avla.app.model.UserSingleton;
import com.avla.app.view.main.PostInfoActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private static final String TAG = "PostAdapter";
    private List<Post> posts;
    private String token;
    private String postId;
    private Context context;
    private UserSingleton userSingleton = UserSingleton.INSTANCE;
    private ArrayList<String> userSavedPost = userSingleton.getSavedPosts();
    RequestOptions requestOptions;
    public PostAdapter(List<Post> posts, String token, Context context) {
        this.posts = posts;
        this.token = token;
        this.context = context;
        requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostAdapter.PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        if(post != null) {

            postId = post.getId();
            if (post.getCreatedBy() != null) {
                holder.postTitle.setText(post.getName());
                holder.postCreator.setText(post.getCreatedBy().getCreatedAt());
            } else {
                holder.postTitle.setText(post.getPost().getName());
                holder.postCreator.setText(post.getPost().getDescription());
            }
            holder.likeCount.setText(String.valueOf(post.getSavedCount()));
            holder.commCount.setText(post.getCommentsCount());
            if (userSavedPost!= null)
            if (userSavedPost.contains(post.getId())) {
                holder.like.setImageDrawable(context.getDrawable(R.drawable.ic_liked));
            } else {
                holder.like.setImageDrawable(context.getDrawable(R.drawable.ic_like));
            }
            holder.like.setOnClickListener(v -> {
                markAsSave(token, post.getId());
                if (userSavedPost.contains(post.getId())) {
                    userSavedPost.remove(post.getId());
                    post.setSavedCount(post.getSavedCount() - 1);
                    notifyDataSetChanged();
                } else {
                    userSavedPost.add(post.getId());
                    post.setSavedCount(post.getSavedCount() + 1);
                    notifyDataSetChanged();

                }

            });
            if (post.getPictureUrl() != null || post.getPost() != null) {
                if (post.getPictureUrl() != null) {
                    Glide.with(context).load(post.getPictureUrl()).apply(requestOptions).into(holder.postImage);
                } else if (post.getPost().getPictureUrl() != null) {
                    Glide.with(context).load(post.getPost().getPictureUrl()).apply(requestOptions).into(holder.postImage);
                }
            } else {
                holder.postImage.setVisibility(View.GONE);
            }
            holder.postItem.setOnClickListener(v -> {
                Intent intent = new Intent(context, PostInfoActivity.class);
                if (post.getId() != null) {
                    intent.putExtra("post", post);
                    intent.putExtra("token", token);
                    intent.putExtra("post_id", post.getId());
                    context.startActivity(intent);
                }else {
                    intent.putExtra("post", post.getPost());
                    intent.putExtra("token", token);
                    intent.putExtra("post_id", post.getPost().getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    private void onClick(View view) {
        switch (view.getId()){
            case R.id.like_img:
                Observable.just((markAsSave(token,postId)))
                        .subscribeOn(Schedulers.io())
                        .subscribe();

        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView postTitle, postCreator, likeCount, commCount;
        ImageView like, comm, postImage;
        View postItem;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postItem = itemView.findViewById(R.id.post_item_view);
            postImage = itemView.findViewById(R.id.post_img1);
            like = itemView.findViewById(R.id.like_img);
            comm = itemView.findViewById(R.id.comm_img);
            postTitle = itemView.findViewById(R.id.post_title);
            postCreator= itemView.findViewById(R.id.post_creator);
            likeCount = itemView.findViewById(R.id.like_count);
            commCount = itemView.findViewById(R.id.comm_count);
        }
    }
    public void addAll(List<Post> newList){
        posts.addAll(newList);
        notifyDataSetChanged();
    }
    public void addAllPost(List<Post> newList){
        posts.clear();
        posts.addAll(newList);
        notifyDataSetChanged();
    }

    private Boolean markAsSave(String token, String postId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<Token> call = service.saveDeletePost(postId, token);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Log.d(TAG, "onResponse:  " + userSavedPost);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });
        return true;
    }
    public void removeItem(int position, String postId) {
        hideDelete(postId);
        posts.remove(position);
        notifyItemRemoved(position);
    }

    private void hideDelete(String postId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<ModelPost> call = service.hideDeletePost(postId, token);

        call.enqueue(new Callback<ModelPost>() {
            @Override
            public void onResponse(Call<ModelPost> call, Response<ModelPost> response) {

               ModelPost modelPost = response.body();
               if (modelPost.getOk()){

               } else {
                   Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
               }
            }

            @Override
            public void onFailure(Call<ModelPost> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public List<Post> getData() {
        return posts;
    }

}
