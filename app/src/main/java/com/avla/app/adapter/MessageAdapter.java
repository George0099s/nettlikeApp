package com.avla.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.R;
import com.avla.app.model.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> mMessages;


    public MessageAdapter(Context context, List<Message> messages) {
        mMessages = messages;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        viewHolder.setMessage(message.getText());
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
        private TextView mUsernameView;
        private TextView mMessageView;

        public ViewHolder(View itemView) {
            super(itemView);

//            mUsernameView = (TextView) itemView.findViewById(R.id.user);
            mMessageView = (TextView) itemView.findViewById(R.id.message_body);
        }


        public void setMessage(String message) {
            if (null == mMessageView) return;
            mMessageView.setText(message);
        }

    }

    public  void  update(Message message){
//        mMessages.add(message);
        mMessages.add(0,message);
//        notifyItemInserted(mMessages.size()-1);
        notifyDataSetChanged();
    }
}
