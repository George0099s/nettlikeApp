package com.nettlike.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nettlike.app.R;
import com.nettlike.app.model.Payload;
import com.nettlike.app.model.UserSingleton;
import com.nettlike.app.view.authorization.signUp.LocationCitiesActivity;

import java.util.ArrayList;
import java.util.List;

public class LocationCountryAdapter extends RecyclerView.Adapter<LocationCountryAdapter.LocationViewHolder>{

    private ArrayList<Payload> counryListName;
    private ArrayList<String> counryListId;
    private ArrayList<String> cityList;
    private Context mContext;
    private Activity activity;


    public LocationCountryAdapter(ArrayList<Payload> counryListName, ArrayList<String> countryListId, ArrayList<String> cityList, Context mContext, Activity activity) {
        this.counryListName = counryListName;
        this.counryListId = countryListId;
        this.cityList = cityList;
        this.mContext = mContext;
        this.activity = activity;
    }
    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item, parent, false);
        return new LocationCountryAdapter.LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
            holder.country.setText(counryListName.get(position).getName());
            holder.country.setOnClickListener(v-> {
                UserSingleton user = UserSingleton.INSTANCE;
                user.setCountry(counryListName.get(position).getName());
                Intent intent = new Intent(mContext, LocationCitiesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("token", activity.getIntent().getStringExtra("token"));
                intent.putExtra("country", counryListName.get(position));
                intent.putExtra("id", counryListId.get(position));
                activity.startActivityForResult(intent, 1);
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

    public void updateList(List<Payload> newCountryList, List<String> CountryIdList){
        counryListName = new ArrayList<>();
        counryListName.addAll(newCountryList);
        notifyDataSetChanged();
    }
}
