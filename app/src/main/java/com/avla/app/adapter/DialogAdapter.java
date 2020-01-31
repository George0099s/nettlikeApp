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
import com.avla.app.fragments.DialogActivity;
import com.avla.app.model.dialog.DiaMember;
import com.avla.app.model.dialog.DiaPayload;
import com.avla.app.model.Token;
import com.avla.app.model.UserSingleton;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

public class DialogAdapter extends  RecyclerView.Adapter<DialogAdapter.DialogViewHolder> {
    private List<DiaPayload> dialogsList;
    private Context context;
    private String token;

    public DialogAdapter(List<DiaPayload> dialogsList, Context context, String token) {
        this.dialogsList = dialogsList;
        this.context = context;
        this.token = token;
    }

    @NonNull
    @Override
    public DialogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_item, parent, false);
        return new DialogAdapter.DialogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DialogViewHolder holder, int position) {
        String createdBy = dialogsList.get(position).getCreatedBy();
        String dialogId = dialogsList.get(position).getId();
        holder.lastMessage.setText(dialogsList.get(position).getLastMessage().getText());
            for (DiaMember member : dialogsList.get(position).getMembers()){
                if(!member.getId().equals(UserSingleton.INSTANCE.getUserId())){
                    holder.senderName.setText(String.format("%s %s",member.getFirstName(), member.getLastName()));
                    Glide.with(context).load(Constants.BASIC_URL + "public_api/account/get_picture?picture_id="+member.getPictureId()).into(holder.senderImageView);
                }
            }
        holder.cardView.setOnClickListener(v -> {
            Observable.just(startNewDia(createdBy, token))
                    .subscribeOn(Schedulers.io())
                    .subscribe();
            Intent intent = new Intent(context, DialogActivity.class);
            intent.putExtra("user id", createdBy);
            intent.putExtra("token", token);
            intent.putExtra("dialog id", dialogId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dialogsList.size();
    }

    public class DialogViewHolder extends RecyclerView.ViewHolder{

        ImageView senderImageView;
        TextView senderName, lastMessage;
        CardView cardView;
        public DialogViewHolder(@NonNull View itemView) {

            super(itemView);
            cardView = itemView.findViewById(R.id.dialog_cardView);
            senderName = itemView.findViewById(R.id.sender_name);
            senderImageView = itemView.findViewById(R.id.sender_img);
            lastMessage = itemView.findViewById(R.id.last_message_body);
        }
    }
    private Boolean startNewDia(String userId, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();
        IServer service = retrofit.create(IServer.class);
        Call<Token> call = service.startDia(userId, token);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {

            }
            @Override
            public void onFailure(Call<Token> call, Throwable t) {

            }
        });
        return null;
    }
}
