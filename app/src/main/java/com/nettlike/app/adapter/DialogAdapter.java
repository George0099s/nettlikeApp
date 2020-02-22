package com.nettlike.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nettlike.app.Constants;
import com.nettlike.app.Interface.IServer;
import com.nettlike.app.R;
import com.nettlike.app.model.UserSingleton;
import com.nettlike.app.model.dialog.DiaMember;
import com.nettlike.app.model.dialog.DiaPayload;
import com.nettlike.app.model.dialog.DialogModel;
import com.nettlike.app.view.DialogActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.DialogViewHolder> {
    private static final String TAG = "DialogAdapter";
    private List<DiaPayload> dialogsList;
    private Context context;
    private String token;
    private String typingDiaId;
    private boolean isTyping = false;

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

        String userPictureId = "";
       if(dialogsList.get(position).getId().equals(typingDiaId)){
            if (isTyping){
                holder.lastMessage.setText(R.string.is_typing_string);
                if(dialogsList.get(position).getUnreadCount() == 0){
                    holder.unreadCount.setVisibility(View.GONE);
                } else {
                    holder.unreadCount.setVisibility(View.VISIBLE);
                }
            } else {
                holder.lastMessage.setText(dialogsList.get(position).getLastMessage().getText());
                if(dialogsList.get(position).getUnreadCount() == 0){
                    holder.unreadCount.setVisibility(View.GONE);
                } else {
                    holder.unreadCount.setVisibility(View.VISIBLE);
                }
            }
        } else {
           holder.lastMessage.setText(dialogsList.get(position).getLastMessage().getText());
           if(dialogsList.get(position).getUnreadCount() == 0){
               holder.unreadCount.setVisibility(View.GONE);
           } else {
               holder.unreadCount.setVisibility(View.VISIBLE);
           }
       }
        for (DiaMember member : dialogsList.get(position).getMembers()){
                if(!member.getId().equals(UserSingleton.INSTANCE.getUserId())){
                    userPictureId = member.getPictureId();
                    holder.senderName.setText(String.format("%s %s",member.getFirstName(), member.getLastName()));
                    Glide.with(context).load(member.getPictureId()).apply(RequestOptions.circleCropTransform()).into(holder.senderImageView);
                }
        }

        String finalUserPictureId = userPictureId;
        holder.dialogLayout.setOnClickListener(v -> {
            Observable.just(startNewDia(createdBy, token))
                    .subscribeOn(Schedulers.io())
                    .subscribe();
            Intent intent = new Intent(context, DialogActivity.class);
            intent.putExtra("user id", createdBy);
            intent.putExtra("picture id", finalUserPictureId);
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
        TextView senderName, lastMessage, unreadCount;
        FrameLayout dialogLayout;
        public DialogViewHolder(@NonNull View itemView) {

            super(itemView);
            unreadCount = itemView.findViewById(R.id.unread_msg_count);
            dialogLayout = itemView.findViewById(R.id.dialog_cardView);
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
        Call<DialogModel> call = service.startDia(userId, token);
        call.enqueue(new Callback<DialogModel>() {
            @Override
            public void onResponse(Call<DialogModel> call, Response<DialogModel> response) {

            }
            @Override
            public void onFailure(Call<DialogModel> call, Throwable t) {

            }
        });
        return null;
    }

    public void update(DiaPayload dialog){
        DiaPayload payload;
        for (int i = 0; i < dialogsList.size(); i++) {
            if (dialogsList.get(i).getId().equals(dialog.getId())) {
                dialogsList.get(i).setLastMessage(dialog.getLastMessage());
                payload = dialogsList.get(i);
                dialogsList.remove(i);
                dialogsList.add(0, payload);
                notifyDataSetChanged();
            }
        }
    }

    public void isTyping(Boolean isTyping, String typingDiaId){
        this.isTyping = isTyping;
        this.typingDiaId = typingDiaId;
        notifyDataSetChanged();
    }

    public void removeItem(int position, String dialogId) {
        deleteDialog(dialogId);
        dialogsList.remove(position);
        notifyItemRemoved(position);
    }

    private void deleteDialog(String dialogId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<DialogModel> call = service.deleteDialog(dialogId, token);

        call.enqueue(new Callback<DialogModel>() {
            @Override
            public void onResponse(Call<DialogModel> call, Response<DialogModel> response) {

                DialogModel modelPost = response.body();
                if (modelPost.getOk()){

                } else {
                    Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DialogModel> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public List<DiaPayload> getData() {
        return dialogsList;
    }
}
