package com.avla.app.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.R;
import com.avla.app.model.UserSingleton;

import java.util.ArrayList;
import java.util.List;

public class LocationCityAdapter  extends RecyclerView.Adapter<LocationCityAdapter.LocationViewHolder>{



    private ArrayList<String> cityListName;
    private Context mContext;
    private SharedPreferences sharedPreferences;

    public LocationCityAdapter (ArrayList<String> cityListName, Context mContext, SharedPreferences sharedPreferences) {

        this.cityListName = cityListName;
        this.mContext = mContext;
        this.sharedPreferences = sharedPreferences;
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
