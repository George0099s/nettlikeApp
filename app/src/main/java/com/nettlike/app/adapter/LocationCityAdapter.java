package com.nettlike.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.R;
import com.nettlike.app.model.UserSingleton;

import java.util.ArrayList;
import java.util.List;

public class LocationCityAdapter  extends RecyclerView.Adapter<LocationCityAdapter.LocationViewHolder>{



    private ArrayList<String> cityListName = new ArrayList<>();
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private Activity activity;

    public LocationCityAdapter(ArrayList<String> cityListName, Context mContext, SharedPreferences sharedPreferences, Activity activity) {

        this.cityListName = cityListName;
        this.mContext = mContext;
        this.sharedPreferences = sharedPreferences;
        this.activity = activity;
    }

    @NonNull
    @Override
    public LocationCityAdapter.LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item, parent, false);
        return new LocationCityAdapter.LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationCityAdapter.LocationViewHolder holder, int position) {
        holder.city.setText(cityListName.get(position));
        holder.city.setOnClickListener(v-> {
            UserSingleton user = UserSingleton.INSTANCE;
            user.setCity(cityListName.get(position));
            activity.finish();
        });
    }

    @Override
    public int getItemCount() {
        return cityListName.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {

        TextView city;
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            city= itemView.findViewById(R.id.country_tv);
        }
    }
    public void updateList(List<String> newCityList){
        cityListName= new ArrayList<>();
        cityListName.addAll(newCityList);
        notifyDataSetChanged();
    }
}
