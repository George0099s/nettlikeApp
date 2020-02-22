package com.nettlike.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nettlike.app.CalendarHelper;
import com.nettlike.app.R;
import com.nettlike.app.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private static final String TAG = "MessageAdapter";
    private List<Message> mMessages;
    private Boolean isTyping = false;
    private CalendarHelper calendarHelper;
    private String lastMessage = "00";
    public MessageAdapter(Context context, List<Message> messages) {
        mMessages = messages;
        calendarHelper = new CalendarHelper();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = -1;
        switch (viewType) {
            case Message.OTHER_MSG:
                layout = R.layout.item_chat_left;
                break;
            case Message.USER_MSG:
                layout = R.layout.item_chat_right;
                break;

        }
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Message message = mMessages.get(position);
        viewHolder.setMessage(message);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return Integer.parseInt(mMessages.get(position).getType());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mMessageView, time;
        private TextView isTypingTV;

        public ViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            isTypingTV = itemView.findViewById(R.id.is_typing);
            mMessageView = itemView.findViewById(R.id.message_body);
        }


        public void setMessage(Message message) {
            String messageTime = null;
            if (null == mMessageView) return;
            messageTime = calendarHelper.getIntervaledMessageTime(message.getCreatedAt());
            String formatedDate = messageTime.substring(messageTime.length() - 4);
            mMessageView.setText(message.getText());
            time.setText(messageTime);
            lastMessage = formatedDate;
        }
    }
    public  void  update(Message message){
       if(message != null){
        mMessages.add(0,message);
        notifyDataSetChanged();
       }
    }
    public void addAll(ArrayList<Message> newList){
        mMessages.addAll(newList);
        notifyDataSetChanged();
    }
//    public void isTyping(Boolean isTyping){
//        this.isTyping = isTyping;
//        notifyDataSetChanged();
//    }
}
