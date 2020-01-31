package com.avla.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.fragments.main.peopleInner.AnotherUserProfileActivity;
import com.avla.app.model.people.People;
import com.avla.app.model.Token;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PeopleAdapter  extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {
    private List<People> people;
    private Context context;
    private String token;
    public PeopleAdapter(Context context, List<People> people, String token) {
        this.context = context;
        this.people = people;
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
        holder.userName.setText(people.get(position).getFirstName() + " " + people.get(position).getLastName());
        holder.aboutUser.setText(people.get(position).getDescription());
        Glide.with(context).load(Constants.BASIC_URL + "public_api/account/get_picture?picture_id="+people.get(position).getPictureId()).into(holder.userImg);
        holder.saveUser.setOnClickListener(v -> saveDeleteUser(people.get(position).getId(), token));
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AnotherUserProfileActivity.class);
            intent.putExtra("user id", people.get(position).getId());
            intent.putExtra("token", token);
            context.startActivity(intent);
        });
    }

    private void saveDeleteUser(String accountId, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();
        IServer service = retrofit.create(IServer.class);
        Call<Token> call = service.saveDeleteUser(accountId, token);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {

            }
            @Override
            public void onFailure(Call<Token> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public class PeopleViewHolder extends RecyclerView.ViewHolder{
        TextView userName, aboutUser;
        List tags;
        CardView cardView;
        ImageView userImg, saveUser;
        public PeopleViewHolder(@NonNull View itemView) {
            super(itemView);
            saveUser = itemView.findViewById(R.id.save_user);
            cardView = itemView.findViewById(R.id.people_cardview);
            userName = itemView.findViewById(R.id.job_title);
            aboutUser = itemView.findViewById(R.id.about_user);
            userImg = itemView.findViewById(R.id.people_img);
        }
    }

    public void addAll(ArrayList<People> newList){
        people.addAll(newList);
        notifyDataSetChanged();
    }
    public void updateList(ArrayList<People> searchedPeople){
        people = new ArrayList<>();
        people.addAll(searchedPeople);
        notifyDataSetChanged();
    }


}
