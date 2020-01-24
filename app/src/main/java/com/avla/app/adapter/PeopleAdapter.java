package com.avla.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.R;
import com.avla.app.model.People;

import java.util.List;

public class PeopleAdapter  extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {
    private List<People> people;
    private Context context;
    public PeopleAdapter(Context context, List<People> people) {
        this.context = context;
        this.people = people;
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
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public class PeopleViewHolder extends RecyclerView.ViewHolder{
        TextView userName, aboutUser;
        List tags;
        public PeopleViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            aboutUser = itemView.findViewById(R.id.about_user);

        }
    }
}
