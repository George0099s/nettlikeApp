package com.nettlike.app.people;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nettlike.app.R;
import com.nettlike.app.model.PayloadTag;
import com.nettlike.app.model.UserSingleton;
import com.nettlike.app.model.people.People;
import com.nettlike.app.people.ui.AnotherUserProfileActivity;
import com.nettlike.app.profile.UserTagAdapter;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {
    private static final String TAG = "PeopleAdapter";
    private List<People> peoples;
    private Context context;
    private String token;
    private List<PayloadTag> tags;
    private UserTagAdapter userTagAdapter;
    private UserSingleton userSingleton = UserSingleton.INSTANCE;
    public PeopleAdapter(Context context, List<People> people, String token) {
        this.context = context;
        this.peoples = people;
        this.token = token;
    }

    @NonNull
    @Override
    public PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_item, parent, false);
        return new PeopleAdapter.PeopleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleViewHolder holder, int position) {

        People people = peoples.get(position);
            JSONArray jsonArray = new JSONArray();
            tags = peoples.get(position).getTag();
            for (int i = 0; i < tags.size(); i++) {
                jsonArray.put(tags.get(i).getName());
            }
            holder.tagRecycler.setHasFixedSize(true);
            holder.tagRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            userTagAdapter = new UserTagAdapter(jsonArray, context);
            holder.tagRecycler.setAdapter(userTagAdapter);
            holder.userName.setText(String.format("%s %s", people.getFirstName(), people.getLastName()));
        holder.aboutUser.setText(people.getJobTitle());
            Glide.with(context).load(people.getPictureUrl()).apply(RequestOptions.circleCropTransform()).into(holder.userImg);
            holder.cardView.setOnClickListener(v -> {
                Intent intent = new Intent(context, AnotherUserProfileActivity.class);
                intent.putExtra("user id", people.getId());
                intent.putExtra("picture id", people.getPictureUrl());
                intent.putExtra("token", token);
                context.startActivity(intent);
            });
    }
    @Override
    public int getItemCount() {
        return peoples.size();
    }

    public class PeopleViewHolder extends RecyclerView.ViewHolder{
        TextView userName, aboutUser;
        CardView cardView;
        ImageView userImg, saveUser;
        RecyclerView tagRecycler;

        public PeopleViewHolder(@NonNull View itemView) {
            super(itemView);
            tagRecycler = itemView.findViewById(R.id.tag_people);
            saveUser = itemView.findViewById(R.id.save_user);
            cardView = itemView.findViewById(R.id.people_cardview);
            userName = itemView.findViewById(R.id.follower_username);
            aboutUser = itemView.findViewById(R.id.about_user);
            userImg = itemView.findViewById(R.id.follower_img);
        }
    }

    public void addAll(ArrayList<People> newList){
        peoples.addAll(newList);
        notifyDataSetChanged();
    }
    public void updateList(ArrayList<People> searchedPeople){
        peoples = new ArrayList<>();
        peoples.addAll(searchedPeople);
        notifyDataSetChanged();
    }


}
