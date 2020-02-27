package com.nettlike.app.posts;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.nettlike.app.CalendarHelper;
import com.nettlike.app.Constants;
import com.nettlike.app.Interface.IServer;
import com.nettlike.app.R;
import com.nettlike.app.model.ModelPost;
import com.nettlike.app.model.PayloadTag;
import com.nettlike.app.model.Post;
import com.nettlike.app.model.Token;
import com.nettlike.app.model.UserSingleton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private static final String TAG = "PostAdapter";
    private List<Post> posts;
    private String token;
    private String postId;
    private Context context;
    private UserSingleton userSingleton = UserSingleton.INSTANCE;
    private ArrayList<String> userSavedPost = userSingleton.getSavedPosts();
    RequestOptions requestOptions;
    private CalendarHelper calendarHelper;
    public PostAdapter(List<Post> posts, String token, Context context) {
        deleteAll();
        this.posts = posts;
        this.token = token;
        this.context = context;
        this.calendarHelper = new CalendarHelper();
        requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(8));
    }

    public PostAdapter() {

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
        Post finalPost;
        if (post.getId() != null){
            finalPost = post;
        } else {
            finalPost = post.getPost();
        }
        if(finalPost != null) {
            Log.d(TAG, "onBindViewHolder: " + finalPost.getTags().size());

            String postId = finalPost.getId();
            holder.like.setOnClickListener(v -> {
                markAsSave(token, postId);
                if (userSavedPost.contains(finalPost.getId())) {
                    userSavedPost.remove(finalPost.getId());
                    finalPost.setSavedCount(finalPost.getSavedCount() - 1);
                    notifyDataSetChanged();
                } else {
                    userSavedPost.add(finalPost.getId());
                    finalPost.setSavedCount(finalPost.getSavedCount() + 1);
                    notifyDataSetChanged();
                }

            });
                holder.postTitle.setText(finalPost.getName());
            holder.postCreator.setText(String.format("%s %s - %s", finalPost.getCreatedBy().getFirstName(), finalPost.getCreatedBy().getLastName(), calendarHelper.getPostTime(finalPost.getCreatedBy().getCreatedAt())));
            holder.likeCount.setText(String.valueOf(finalPost.getSavedCount()));
            holder.commCount.setText(finalPost.getCommentsCount());
            if (userSavedPost != null)
            if (userSavedPost.contains(finalPost.getId())) {
                holder.like.setImageDrawable(context.getDrawable(R.drawable.ic_liked));
            } else {
                holder.like.setImageDrawable(context.getDrawable(R.drawable.ic_like));
            }
            if (finalPost.getPictureUrl() != null) {
                    Glide.with(context).load(finalPost.getPictureUrl()).apply(requestOptions).into(holder.postImage);
            } else {
                holder.postImage.setVisibility(View.GONE);
            }
            holder.postItem.setOnClickListener(v -> {
                ArrayList<String> tags = new ArrayList<>();
                List<PayloadTag> tagsList = finalPost.getTags();
                for (int i = 0; i < tagsList.size(); i++) {
                    tags.add(tagsList.get(i).getName());
                }
                Intent intent = new Intent(context, PostInfoActivity.class);
                    intent.putExtra("post", finalPost);
                    intent.putExtra("token", token);
                    intent.putExtra("post_id", postId);
                intent.putExtra("tags_array", tags);
                    intent.putExtra("post_picture_url", finalPost.getPictureUrl());
                    context.startActivity(intent);
            });
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

    public void deleteAll() {
        if (posts != null)
            posts.clear();
        notifyDataSetChanged();
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
