package com.avla.app.view.main;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.avla.app.R;
import com.avla.app.model.Event;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class EventInfoActivity extends AppCompatActivity {
    private static final String TAG = "EventInfoActivity";
    private Event event;
    private TextView eventTitle, eventStartTime, eventAddress, eventText;
    private ImageView eventPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        initViews();
    }

    private void initViews() {
        event = getIntent().getParcelableExtra("event");
        eventPicture = findViewById(R.id.picture_info_event);
        eventTitle = findViewById(R.id.title_event_info);
        eventText = findViewById(R.id.text_event_info);
        eventAddress = findViewById(R.id.address_event_info);
        eventStartTime = findViewById(R.id.start_time_event_info);
        eventTitle.setText(event.getName());
        eventStartTime.setText(event.getStartTime());
        eventAddress.setText(event.getAddress());
        eventText.setText(event.getDescription());
        if (getIntent().getStringExtra("event_pictures") != null) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
            Glide.with(this).load(getIntent().getStringExtra("event_pictures")).apply(requestOptions).into(eventPicture);
        }
    }

}
