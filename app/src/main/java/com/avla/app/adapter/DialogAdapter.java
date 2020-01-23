package com.avla.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.model.DialogModel;
import com.avla.app.R;

import java.util.List;

public class DialogAdapter extends  RecyclerView.Adapter<DialogAdapter.DialogViewHolder> {
    private List<DialogModel.Payload> dialogsList;
    private Context context;

    public DialogAdapter(List<DialogModel.Payload> dialogsList, Context context) {
        this.dialogsList = dialogsList;
        this.context = context;
    }

    @NonNull
    @Override
    public DialogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_item, parent, false);
        return new DialogAdapter.DialogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DialogViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return dialogsList.size();
    }

    public class DialogViewHolder extends RecyclerView.ViewHolder{

        ImageView senderImageView;
        TextView senderName, lastMessage;
        public DialogViewHolder(@NonNull View itemView) {

            super(itemView);
            senderName = itemView.findViewById(R.id.sender_name);
            senderImageView = itemView.findViewById(R.id.sender_img);
            lastMessage = itemView.findViewById(R.id.last_message_body);
        }
    }
}
