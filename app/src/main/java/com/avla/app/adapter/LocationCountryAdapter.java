package com.avla.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.R;
import com.avla.app.fragments.SignUp.LocationCitiesActivity;
import com.avla.app.model.UserSingleton;

import java.util.ArrayList;
import java.util.List;

public class LocationCountryAdapter extends RecyclerView.Adapter<LocationCountryAdapter.LocationViewHolder>{

    private ArrayList<String> counryListName;
    private ArrayList<String> counryListId;
    private ArrayList<String> cityList;
    private Context mContext;
    private SharedPreferences sharedPreferences;

    public LocationCountryAdapter(ArrayList<String> counryListName, ArrayList<String> countryListId, ArrayList<String> cityList, Context mContext,  SharedPreferences sharedPrefs) {
        this.counryListName = counryListName;
        this.counryListId = countryListId;
        this.cityList = cityList;
        this.mContext = mContext;
        this.sharedPreferences = sharedPrefs;
    }
    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item, parent, false);
        return new LocationCountryAdapter.LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
            holder.country.setText(counryListName.get(position));
        holder.country.setOnClickListener(v-> {
            UserSingleton user = UserSingleton.INSTANCE;
            user.setCountry(counryListName.get(position));
            Intent intent = new Intent(mContext, LocationCitiesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("country", counryListName.get(position));
            intent.putExtra("id", counryListId.get(position));
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return counryListName.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {

        TextView country;
        TextView city;
        public LocationViewHolder(@NonNull View itemView) {

            super(itemView);
            country= itemView.findViewById(R.id.country_tv);
            city= itemView.findViewById(R.id.country_tv);

        }
    }

    public void updateList(List<String> newCountryList){
        counryListName = new ArrayList<>();
        counryListName.addAll(newCountryList);
        notifyDataSetChanged();
    }
}
