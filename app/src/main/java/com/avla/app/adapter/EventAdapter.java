package com.avla.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.model.Event;
import com.avla.app.model.ModelEvent;
import com.avla.app.view.main.EventInfoActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private static final String TAG = "EventAdapter";
    private  List<Event> events;
    private Context context;
    private RequestOptions requestOptions;
    private String token;
    public EventAdapter(List<Event> events, Context context, String token) {
        this.events = events;
        this.context = context;
        this.token = token;
        requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventAdapter.EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventTitle.setText(event.getName());
        holder.eventDate.setText(event.getStartDate());
        holder.eventStartTime.setText(event.getStartTime());
        if (event.getPhotos() != null && event.getPhotos().size() != 0) {
            Glide.with(context).load(event.getPhotos().get(0)).apply(requestOptions).into(holder.eventPhoto);
        }
        holder.eventItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventInfoActivity.class);
            intent.putExtra("event", event);
            if (event.getPhotos().size() != 0)
            intent.putExtra("event_pictures", event.getPhotos().get(0));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
       TextView eventTitle, eventDate, eventAddress, eventStartTime;
       ImageView interestedInEvent, eventPhoto;
       View eventItem;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventPhoto = itemView.findViewById(R.id.event_photo);
            eventItem = itemView.findViewById(R.id.event_item);
            eventTitle = itemView.findViewById(R.id.event_title);
            eventDate= itemView.findViewById(R.id.event_date);
            eventStartTime = itemView.findViewById(R.id.event_start_time);
            interestedInEvent = itemView.findViewById(R.id.save_event);
        }
    }
    public void removeItem(int position, String eventId) {
        hideDelete(eventId);
        events.remove(position);
        notifyItemRemoved(position);
    }

    private void hideDelete(String postId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<ModelEvent> call = service.deleteEvent(postId, token);

        call.enqueue(new Callback<ModelEvent>() {
            @Override
            public void onResponse(Call<ModelEvent> call, Response<ModelEvent> response) {
                Log.d(TAG, "onResponse: " + call.request().url());
                                ModelEvent modelEvent = response.body();
//                if (modelEvent.getOk()){
//
//                } else {
//                    Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onFailure(Call<ModelEvent> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public List<Event> getData() {
        return events;
    }
}
